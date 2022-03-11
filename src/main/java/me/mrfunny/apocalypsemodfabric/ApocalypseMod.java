package me.mrfunny.apocalypsemodfabric;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApocalypseMod implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("apomod");

    @Override
    public void onInitialize() {
        LOGGER.info("Apocalypse mod initializing");
    }
}
