package provider;

import configs.Config;
import provider.nx.NXFileDataProvider;
import provider.wz.WzFileDataProvider;
import provider.xml.XMLFileDataProvider;
import tools.data.BufferedRandomAccessFile;
import tools.data.MaplePacketReader;
import tools.data.RandomAccessByteStream;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MapleDataProviderFactory {

    private static final java.util.Scanner scanner = new java.util.Scanner(System.in);
    private static String WZPATH = Config.getProperty("wzpath", "Data");

    public static void init() {
        File in = new File(WZPATH);
        if (!in.exists()) {
            System.err.print("请输入WZ资料夹路径:");
            WZPATH = scanner.next().replace("\\", "\\\\");
            Config.setProperty("wzpath", WZPATH);
            init();
        }
    }

    private static Map<File, MapleDataProvider> CACHE = new HashMap();

    private static MapleDataProvider getWZ(String name) {
        File in = new File(WZPATH + File.separator + name);
        if (!in.exists()) {
            in = new File(WZPATH + File.separator + name + ".wz");
            if (!in.exists()) {
                in = new File(WZPATH + File.separator + name + ".nx");
                if (!in.exists()) {
                    throw new RuntimeException("檔案不存在" + in.getPath());
                }
            }
        }
        if (CACHE.containsKey(in)) {
            return CACHE.get(in);
        }
        File checkFile;
        if (in.isDirectory() && name.matches("^([A-Za-z]+)$")) {
            checkFile = new File(in.getPath());// + File.separator + in.getName() + ".wz");
            if (!checkFile.exists()) {
                checkFile = new File(in.getPath() + File.separator + in.getName() + ".nx");
                if (!checkFile.exists()) {
                    throw new RuntimeException("檔案不存在" + checkFile.getPath());
                }
            }
        } else {
            checkFile = in;
        }
        MapleDataProvider fileData;
        if (checkFile.isDirectory()) {
            fileData = new XMLFileDataProvider(in);
        } else {
            try (BufferedRandomAccessFile raf = new BufferedRandomAccessFile(checkFile, "r")) {
                MaplePacketReader lea = new MaplePacketReader(new RandomAccessByteStream(raf));
                String magic = lea.readAsciiString(4);
                raf.close();
                if (magic.equalsIgnoreCase("PKG1")) {
                    fileData = new WzFileDataProvider(in);
                } else if (magic.equalsIgnoreCase("PKG4")) {
                    fileData = new NXFileDataProvider(in);
                } else {
                    throw new RuntimeException("不支援這個" + magic + "格式檔案" + checkFile.getPath());
                }
            } catch (Exception e) {
                throw new RuntimeException("讀取檔案時出錯", e);
            }
        }
        CACHE.put(in, fileData);
        return fileData;
    }

    public static MapleDataProvider getEffect() {
        return getWZ("Effect");
    }

    public static MapleDataProvider getItem() {
        return getWZ("Item");
    }

    public static MapleDataProvider getCharacter() {
        return getWZ("Character");
    }

    public static MapleDataProvider getSkill() {
        return getWZ("Skill");
    }

    public static MapleDataProvider getString() {
        return getWZ("String");
    }

    public static MapleDataProvider getEtc() {
        return getWZ("Etc");
    }

    public static MapleDataProvider getMob() {
        return getWZ("Mob");
    }

    public static MapleDataProvider getNpc() {
        return getWZ("Npc");
    }

    public static MapleDataProvider getMap() {
        return getWZ("Map");
    }

    public static MapleDataProvider getReactor() {
        return getWZ("Reactor");
    }

    public static MapleDataProvider getQuest() {
        return getWZ("Quest");
    }

}
