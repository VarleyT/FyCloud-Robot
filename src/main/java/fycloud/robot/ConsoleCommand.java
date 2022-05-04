//package fycloud.robot;
//
//import lombok.extern.slf4j.Slf4j;
//import love.forte.simbot.api.sender.BotSender;
//import love.forte.simbot.core.SimbotContext;
//
//import java.util.Arrays;
//import java.util.Scanner;
//
///**
// * @author 19634
// * @version 1.0
// * @date 2022/4/23 12:26
// */
//@Slf4j
//public class ConsoleCommand {
//    enum CommandsEnum {
//        send("send");
//
//        CommandsEnum(String send) {
//
//        }
//    }
//
//    public static void run(SimbotContext context) {
//        BotSender sender = context.getBotManager().getDefaultBot().getSender();
//        Scanner scanner = new Scanner(System.in);
//        String input = null;
//        while (true) {
//            input = scanner.nextLine();
//            if (input != null) {
//                try {
//                    String[] Command = input.split(" ");
//                    if (Command[0].equals("send")) {
//                        if (Command[1] != null || Command[2] != null || Command[3] != null) {
//                            Sender(Command[1], Command[2], Command[3], sender);
//                        }else {
//                            log.error("参数错误！！ send [group|private] (code) (message)");
//                        }
//                    } else {
//                        log.error("该命令不存在！" + Arrays.stream(CommandsEnum.values()));
//                    }
//                } catch (Exception e){
//                    log.error("命令错误！！");
//                }
//
//            } else {
//                log.error("读取信息为空！");
//            }
//        }
//    }
//
//    private static void Sender(String Type, String Code, String text, BotSender sender) {
//        if (Type.equals("group")) {
//            sender.SENDER.sendGroupMsg(Code, text);
//        } else if (Type.equals("private")) {
//            sender.SENDER.sendPrivateMsg(Code, text);
//        } else {
//            log.warn("参数[type]错误！应为 [group|private]");
//        }
//    }
//}
