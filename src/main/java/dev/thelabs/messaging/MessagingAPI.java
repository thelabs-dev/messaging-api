package dev.thelabs.messaging;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import dev.thelabs.messaging.helpres.SlackHelper;
import dev.thelabs.messaging.helpres.TelegramHelper;
import dev.thelabs.messaging.http.Proxy;

public class MessagingAPI {
    static String VERSION = "MessagingAPI v2.0";

    static String PROXY_USER = "proxyuser";
    static String PROXY_PASS = "proxypass";
    static String PROXY_TYPE = "proxytype";
    static String PROXY_PORT = "proxyport";
    static String PROXY_HOST = "proxyhost";
    static String PROXY_DOMI = "proxydomain";
    static String DELAY     = "delay";
    static String BOTNAME    = "botname";
    static String ICONURL    = "iconurl";

    static String PLATFORM   = "platform";
    static String METHOD     = "method";

    static String TOKEN      = "token";
    static String MESSAGE    = "message";
    static String CHANNEL    = "channel";
    static String CHAT       = "chat";

    public static Boolean debug = false;

    
    public static void main(String args[]){
        System.out.println(VERSION);
        // Options 
        Options options = new Options();
        options.addOption(PROXY_USER, true, "Usuario para autenticarse en el proxy");
        options.addOption(PROXY_PASS, true, "Contraseña para autenticarse en el proxy");
        //options.addOption(PROXY_TYPE, true, "");
        options.addOption(PROXY_PORT, true, "Puerto del proxy");
        options.addOption(PROXY_HOST, true, "Host del proxy");
        options.addOption(PROXY_DOMI, true, "Dominio del usuario en el proxy");
        options.addOption(DELAY, true, "Tiempo de demora para enviar el mensaje (Slack)");
        options.addOption(BOTNAME, true, "Nombre del bot que se mostrará al recibir el mensaje (Slack)");
        options.addOption(ICONURL, true, "Icono del bot que se mostrará al recibir el mensaje (Slack)");

        options.addOption(PLATFORM, true, "Plataforma a realizar en envío");
        options.addOption(METHOD, true, "Método a ejecutar");

        options.addOption(TOKEN, true, "Token de la plataforma");
        options.addOption(MESSAGE, true, "Mensaje");
        options.addOption(CHANNEL, true, "Canal");

        options.addOption("d", true, "Debug");

        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse( options, args );
            int platform = 0;
            int method = 1;
            if( line.hasOption(PLATFORM)) {
                String inPlatform = line.getOptionValue(PLATFORM);
                if (inPlatform.equalsIgnoreCase("slack")){
                    platform = 1;
                }else if (inPlatform.equalsIgnoreCase("telegram")){
                    platform = 2;
                }
            }

            if(line.hasOption("d")) debug = true;

            Proxy proxy = null;
            if( line.hasOption(PROXY_HOST) ) {
                // Configuracion del proxy
                proxy = new Proxy();
                if( line.hasOption(PROXY_HOST)) proxy.hostname = line.getOptionValue(PROXY_HOST);
                if( line.hasOption(PROXY_USER)) proxy.username = line.getOptionValue(PROXY_USER);
                if( line.hasOption(PROXY_PASS)) proxy.password = line.getOptionValue(PROXY_PASS);
                if( line.hasOption(PROXY_PORT)) proxy.port = Integer.parseInt(line.getOptionValue(PROXY_PORT));
                if( line.hasOption(PROXY_DOMI)) proxy.userdomain = line.getOptionValue(PROXY_DOMI);
            }
            
            int delay = 0;
            if( line.hasOption(DELAY)) delay = Integer.parseInt(line.getOptionValue(DELAY));
            String botName = "";
            if( line.hasOption(BOTNAME)) botName = line.getOptionValue(BOTNAME);
            String iconUrl = "";
            if( line.hasOption(ICONURL)) iconUrl = line.getOptionValue(ICONURL);

            switch (platform) {
                case 1: // SLACK
                    if (debug) System.out.println("slack");
                    if (!(line.hasOption(TOKEN) && line.hasOption(MESSAGE) && line.hasOption(CHANNEL))){
                        System.out.println("ERROR: Falta el token, mensaje o canal");
                        help(options);
                        return;
                    }
                   
                    String token = line.getOptionValue(TOKEN);
                    String message = line.getOptionValue(MESSAGE);
                    String channel = line.getOptionValue(CHANNEL);
                    switch (method) {
                        case 1: // SendMessage
                            ResponseApi response = SlackHelper.sendMessage(debug, proxy, token, channel, message, delay, botName,iconUrl);
                            if(response.success){
                                System.out.println("Se envió el mensaje por Slack al canal " + channel);
                            }else{
                                System.out.println("ERROR al enviar el mensaje por Slack al canal " + channel);
                                System.out.println(response.error_message);
                            }
                            break;
                    
                        default:
                            System.out.println("ERROR: El metodo no es reconocido");
                            help(options);
                            return;
                    }
                    break;
                
                case 2: // TELEGRAM
                    if (debug) System.out.println("telegram");
                    if (!(line.hasOption(TOKEN) && line.hasOption(MESSAGE) && line.hasOption(CHANNEL))){
                        System.out.println("ERROR: Falta el token, mensaje o chat");
                        help(options);
                        return;
                    }
                
                    token = line.getOptionValue(TOKEN);
                    message = line.getOptionValue(MESSAGE);
                    channel = line.getOptionValue(CHANNEL);
                    switch (method) {
                        case 1: // SendMessage
                            ResponseApi response = TelegramHelper.sendMessage(debug, proxy, token, channel, message);
                            if(response.success){
                                System.out.println("Se envió el mensaje por Telegram al chat " + channel);
                            }else{
                                System.out.println("ERROR al enviar el mensaje por Telegram al chat " + channel);
                                System.out.println(response.error_message);
                            }
                            break;
                    
                        default:
                            System.out.println("ERROR: El metodo no es reconocido");
                            help(options);
                            return;
                    }

                    break;
                default:
                    System.out.println("ERROR: La platafroma no es reconocida");
                    help(options);
                    return;
                }
            }
        catch(ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
    
    static void help( Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("help", options);
    }
}