
# Variables
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_FILE="/backup/mysql_data-$DATE.tar.gz"
LOG_FILE="/backup/backup.log"

# Crear un backup comprimido del volumen montado
echo "$(date +"%Y-%m-%d %H:%M:%S") - Iniciando respaldo de MySQL..." >> "$LOG_FILE"
tar -czf "$BACKUP_FILE" -C /data . >> "$LOG_FILE" 2>&1
if [ $? -eq 0 ]; then
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Backup comprimido creado: $BACKUP_FILE" >> "$LOG_FILE"
else
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Error al crear el backup" >> "$LOG_FILE"
    exit 1
fi

# Subir el backup a Google Drive
echo "$(date +"%Y-%m-%d %H:%M:%S") - Subiendo el backup a Google Drive..." >> "$LOG_FILE"
rclone copy "$BACKUP_FILE" gdrive:/docker-backups/ >> "$LOG_FILE" 2>&1
if [ $? -eq 0 ]; then
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Backup subido correctamente a Google Drive" >> "$LOG_FILE"
else
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Error al subir el backup a Google Drive" >> "$LOG_FILE"
    exit 1
fi

# Eliminar backups locales mayores a 7 días
echo "$(date +"%Y-%m-%d %H:%M:%S") - Eliminando backups locales mayores a 7 días..." >> "$LOG_FILE"
find /backup -type f -name "*.tar.gz" -mtime +7 -exec rm {} \; >> "$LOG_FILE" 2>&1
if [ $? -eq 0 ]; then
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Backups locales eliminados" >> "$LOG_FILE"
else
    echo "$(date +"%Y-%m-%d %H:%M:%S") - Error al eliminar los backups locales" >> "$LOG_FILE"
    exit 1
fi

# Log de salida
echo "$(date +"%Y-%m-%d %H:%M:%S") - Backup completado: $BACKUP_FILE y subido a Google Drive." >> "$LOG_FILE"