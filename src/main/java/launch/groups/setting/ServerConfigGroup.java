package launch.groups.setting;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.scroll.WebScrollPane;
import configs.ServerConfig;
import constants.ServerConstants;

import java.awt.*;

public class ServerConfigGroup extends AbstractConfigGroup {


    public ServerConfigGroup(ConfigPanel owner) {
        super(owner, "服务端配置");
    }

    @Override
    public Component getPreview() {
        TitleWebPanel titleWebPanel1 = new TitleWebPanel("基本设置");
        titleWebPanel1.add(new GroupPanel(false,
                new ConfigComponent("游戏名称", "login.server.name", ServerConfig.LOGIN_SERVERNAME),
                new ConfigComponent("游戏补丁版本", "login.server.minor", ServerConfig.MapleMinor),
                new ConfigComponent("频道选择界面公告", "login.server.eventmessage", ServerConfig.LOGIN_EVENTMESSAGE),
                new GroupPanel(
                        new ConfigComponent("连接服务器Ip(留空自动获取)", "server.host", "127.0.0.1"),
                        new ConfigComponent("强制外网", "server.forceInternetIP", false)
                ),
                new ConfigComponent("频道数量", "channel.server.ports", ServerConfig.CHANNEL_PORTS),
                new ConfigComponent("游戏服务器名称", "login.server.flag", ServerConfig.LOGIN_SERVERFLAG,
                        getAllServerName()),
                new ConfigComponent("游戏服务器状态", "login.server.status", ServerConfig.LOGIN_SERVERSTATUS,
                        getAllServerStatus()),
                new GroupPanel(
                        new ConfigComponent("测试机", "tespia", ServerConfig.TESPIA),
                        new ConfigComponent("多人测试限制(仅测试机)", "testpia.multiplayer", ServerConfig.MULTIPLAYER_TEST)
                )
        ).setMargin(5));

        TitleWebPanel titleWebPanel2 = new TitleWebPanel("数据库连接设置");
        titleWebPanel2.add(new GroupPanel(false,
                new ConfigComponent("IP", "db.ip", ServerConfig.DB_IP),
                new ConfigComponent("连接端口", "db.port", ServerConfig.DB_PORT),
                new ConfigComponent("数据库名称", "db.name", ServerConfig.DB_NAME),
                new ConfigComponent("账号", "db.user", ServerConfig.DB_USER),
                new ConfigComponent("密码", "db.password", ServerConfig.DB_PASSWORD)
//                new GroupPanel(new WebButton("測試連接") {
//                    {
//                        setPreferredWidth(100);
//                        addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                new SwingWorker() {
//                                    @Override
//                                    protected Object doInBackground() throws Exception {
//                                        setEnabled(false);
//                                        String oldtext = getText();
//                                        setText("正在測試...");
//                                        owner.applyChange();
//                                        DatabaseConnection.DataBaseStatus status = DatabaseConnection.TestConnection();
//                                        WebOptionPane.showMessageDialog(null, status.name());
//                                        Start.getInstance().setDataBaseStatus(status);
//                                        setText(oldtext);
//                                        setEnabled(true);
//                                        return null;
//                                    }
//                                }.execute();
//                            }
//                        });
//                    }
//                }).setMargin(0, 150, 0, 0)
        ).setMargin(5));

        TitleWebPanel titleWebPanel3 = new TitleWebPanel("其他设置");
        titleWebPanel3.add(new GroupPanel(false,
                new ConfigComponent("脚本位置", "world.server.scriptspath", ServerConfig.WORLD_SCRIPTSPATH),
                new ConfigComponent("客制化脚本位置", "world.server.scriptspath2", ServerConfig.WORLD_SCRIPTSPATH2)
        ).setMargin(5));

        WebScrollPane webScrollPane = new WebScrollPane(new GroupPanel(5, false, titleWebPanel1, titleWebPanel2, titleWebPanel3), false);
        webScrollPane.createHorizontalScrollBar();
        webScrollPane.getViewport().setOpaque(false);
        return webScrollPane;
    }

    private String getAllServerName() {
        StringBuilder sb = new StringBuilder();
        for (ServerConstants.MapleServerName serverName : ServerConstants.MapleServerName.values()) {
            sb.append(serverName.name()).append(",");
        }
        return sb.toString();
    }

    private String getAllServerStatus() {
        StringBuilder sb = new StringBuilder();
        for (ServerConstants.MapleServerStatus status : ServerConstants.MapleServerStatus.values()) {
            sb.append(status.name()).append(",");
        }
        return sb.toString();
    }


}
