Abrir terminal y ejecutar 
docker compose up --build

Realizar un Backup de la Base de Datos
```
docker exec -it <nombre-del-contenedor> mysqldump -u root -p prueba > backup.sql
```

Usando un volumen local:
```
docker run --rm -v $(pwd):/backup --network app-network mysql:8.0.33 \
    bash -c "mysqldump -h contexto_invest -u root -p1234 prueba > /backup/backup.sql"
```
Esto creará un archivo backup.sql en el directorio actual de tu máquina anfitriona.

Restaurar backup 

Cargar el archivo SQL en el contenedor:

```
docker exec -i <nombre-del-contenedor> mysql -u root -p prueba < backup.sql
```

Pasos:
Navega al directorio donde tienes el archivo backup.sql:
```
cd /ruta/al/directorio/del/backup
```
Ejecuta el comando
```
docker exec -i <nombre-del-contenedor> mysql -u root -p prueba < backup.sql
```

Pasos para reutilizar el volumen existente

``` docker volume ls```

Busca el nombre del volumen que configuraste, por ejemplo, mysql_data.

Asocia el volumen al nuevo contenedor

```
version: '3'
services:
  mysql:
    image: mysql:8.0.33
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 1234
      MYSQL_DATABASE: prueba
    volumes:
      - mysql_data:/var/lib/mysql # Usa el volumen existente
volumes:
  mysql_data:  # Este nombre debe coincidir
  ```

  para conectarse a la bd del contenedor se debe abrir workbench

New Connection -> hostname: localhost Port: 3307 
Username: root password: 1234; 

## backup online
instalar rclone en local

https://rclone.org/install/

w10 
```winget install Rclone.Rclone```

ejecutar en cmd y agregar un remote
```rclone config```

ubicar archivo de config
```rclone config file``` \
devuelve donde se encuentra el archivo rclone.conf, hay que colocarlo en 
```C:\Users\NOMBREUSUARIO\.config\rclone```
Requisitos: hay que tener instalado rclone en la pc y configurar el acceso remoto. 

Para hacer uso del backup online al contenedor:
Usa rclone ls para verificar que el archivo de backup está en Google Drive.
```rclone ls gdrive:/docker-backups/```

Usa rclone copy para descargar el archivo de backup.
```rclone copy gdrive:/docker-backups/{nombre_del_archivo.tar.gz} /tmp/backup/```
Cambia /tmp/backup/ por la ruta en tu contenedor donde deseas que se guarde el archivo descargado o dejala por default.
 
Antes de restaurar un respaldo que contenga datos de la base de datos directamente en el directorio de MySQL (/var/lib/mysql), es importante detener el servicio MySQL para evitar conflictos entre la base de datos en ejecución y los archivos que estás restaurando.

``` docker stop contexto_invest```

##############################################
OPCIONAL: 
Acceder al contenedor MySQL
``` docker exec -it <nombre_del_contenedor> bash```
Acceder al cliente de MySQL
```mysql -u root -p```

Eliminar la base de datos
```DROP DATABASE prueba;```

##############################################

Descomprimir el archivo y restaurarlo en MySQL (cuando descomprimes un archivo utilizando el comando tar con la opción -xzf, si los archivos que estás descomprimiendo ya existen en el directorio de destino, se reemplazarán por los nuevos archivos del respaldo.)
```tar -xzf /tmp/backup/mysql_data-2024-11-23_20-50-11.tar.gz -C /var/lib/mysql```

Los archivos restaurados deben tener los permisos adecuados para que MySQL los pueda leer y escribir correctamente. Al descomprimir el archivo, los permisos y la propiedad del archivo deben coincidir con los que MySQL espera.

Si tienes problemas con los permisos, puedes ajustar la propiedad de los archivos usando chown:
``` chown -R mysql:mysql /var/lib/mysql```

Una vez que hayas restaurado los archivos y asegurado que los permisos estén correctos, reinicia el contenedor de MySQL para que reconozca los archivos restaurados.
``` docker start contexto_invest```