package com.mygdx.blackjack.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.blackjack.Blackjack;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                // return new GwtApplicationConfiguration(true);
                // Fixed size application:
                return new GwtApplicationConfiguration(Blackjack.WIDTH, Blackjack.HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Blackjack();
        }
}