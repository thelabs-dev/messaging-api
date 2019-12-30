# MessagingAPI [![Build Status](https://travis-ci.org/thelabs-dev/messaging-api.svg?branch=master)](https://travis-ci.org/thelabs-dev/messaging-api)
## Descripción
Esta herramienta esta pensada para enviar mensajes a plataformas externas, por ejemplo, el envío de mensajes a Slack, Telegram, WhatsApp.
La herramienta se puede usar tanto desde línea de comando o importando el jar a un proyecto java.


## Ejemplo de uso
### Línea de comando
	$ java -jar MessagingAPI.jar 


	$ java -jar MessagingAPI.jar -platform slack -token xoxp-XXXXXXXXXXX-XXXXXXXXXXX-XXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -channel #varios -message "Hola Mundo!"

## Parámetros
```
 -channel <arg>       Canal
 -d <arg>             Debug
 -message <arg>       Mensaje
 -method <arg>        Método a ejecutar
 -platform <arg>      Plataforma a realizar en envío
 -proxydomain <arg>   Dominio del usuario en el proxy
 -proxyhost <arg>     Host del proxy
 -proxypass <arg>     Contraseña para autenticarse en el proxy
 -proxyport <arg>     Puerto del proxy
 -proxyuser <arg>     Usuario para autenticarse en el proxy
 -token <arg>         Token de la plataforma
 -delay <arg>         Tiempo de espera antes de mandar el mensaje
```

## Tips
- Se puede enviar \n que será interpretado como un salto de línea

## Sporte
- Slack
- Proxy
  
## Configuración
Compilacion con:

    $ mvn package


