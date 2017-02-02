package io.deltawave.poke.pokemongo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class BattleActivity extends AppCompatActivity {

    ImageView supportPoke;
    ImageView opposingPoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        supportPoke = (ImageView) findViewById(R.id.battle_supporting_poke);
        opposingPoke = (ImageView) findViewById(R.id.battle_opposing_poke);

    }
}
