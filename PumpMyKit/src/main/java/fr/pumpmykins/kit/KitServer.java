package fr.pumpmykins.kit;

import java.io.File;

public class KitServer extends KitCommon {

    @Override
    public void preInit(File configFile)
    {
        super.preInit(configFile);
        System.out.println("pre init côté serveur");
    }
 
    @Override
    public void init()
    {
        super.init();
    }
}
