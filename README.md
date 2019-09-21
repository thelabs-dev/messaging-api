# MessagingAPI
## Descripción
Esta herramienta esta pensada para enviar mensajes a plataformas externas, por ejemplo, el envío de mensajes a Slack, Telegram, WhatsApp.
La herramienta se puede usar tanto desde línea de comando o importando el jar a un proyecto java.


## Ejemplo de uso
### Línea de comando
	$ java -jar MessagingAPI.jar 


	$ java -jar MessagingAPI.jar -platform slack -token xoxp-XXXXXXXXXXX-XXXXXXXXXXX-XXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -channel #varios -message "Hola Mundo!"

## Sporte
- Slack
- Proxy
  
## Configuración
Compilacion con:

    $ mvn package


