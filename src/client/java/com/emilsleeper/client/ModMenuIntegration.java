package com.emilsleeper.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChecker;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public UpdateChecker getUpdateChecker() {
        return ModMenuApi.super.getUpdateChecker();
    }
}