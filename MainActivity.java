package ujian.castlegame;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	// game logic and data here
	private final CastleGame game = new CastleGame();
	private StartingArmyType startingArmy = StartingArmyType.CAVALRY_VS_ARCHER;

	// UI components
	private Button btnBattle, btnCastle1, btnCastle2;
	private ImageView imgPlayer1, imgPlayer2, imgWinPlayer1, imgWinPlayer2;
	private TextView txtPlayer1, txtPlayer2, txtInfo, txtTitle;

	@SuppressLint("SourceLockedOrientationActivity")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get all the important UI components from layout
		txtPlayer1 = findViewById(R.id.text_info1);
		txtPlayer2 = findViewById(R.id.text_info2);
		txtInfo = findViewById(R.id.text_info);
		txtTitle = findViewById(R.id.text_title);
		imgPlayer1 = findViewById(R.id.image_player1);
		imgPlayer2 = findViewById(R.id.image_player2);
		imgWinPlayer1 = findViewById(R.id.image_win1);
		imgWinPlayer2 = findViewById(R.id.image_win2);
		btnBattle = findViewById(R.id.button_start);
		btnCastle1 = findViewById(R.id.button_cycle1);
		btnCastle2 = findViewById(R.id.button_cycle2);

		// add listener to buttons
		btnBattle.setOnClickListener(this);
		btnCastle1.setOnClickListener(this);
		btnCastle2.setOnClickListener(this);

		refresh();
	}

	/** Redraws the images and texts so that it is match to current game state.
	 * The state changes everytime user click the Button. */
	public void refresh() {
		// hide win icon for both players
		imgWinPlayer1.setVisibility(View.GONE);
		imgWinPlayer2.setVisibility(View.GONE);

		switch (game.getState()) {
		case STATE_PREPARATION:
			game.init(startingArmy);
			txtTitle.setText("It's Time To Show Your Might!!");
			txtInfo.setText("Click START to show the armies");
			btnBattle.setText("START");
			txtPlayer1.setText("");
			txtPlayer2.setText("");
			break;

		case STATE_BEFORE_BATTLE:
			txtTitle.setText("Let's The Battle Begin!");
			txtInfo.setText("Click BATTLE! to commence the battle");
			btnBattle.setText("BATTLE");
			txtPlayer1.setText(game.getPlayer1Castle().getArmyList());
			txtPlayer2.setText(game.getPlayer2Castle().getArmyList());
			break;

		case STATE_AFTER_BATTLE:

			// do the battle here
			BattleResult battleResult = game.battle();

			txtTitle.setText("After Battle Result");
			btnBattle.setText("CREATE NEW");
			txtPlayer1.setText(game.getPlayer1Castle().getAfterBattleReport());
			txtPlayer2.setText(game.getPlayer2Castle().getAfterBattleReport());
			String result = "Battle ends with Draw";
			if (battleResult == BattleResult.PLAYER_1_WIN) {
				result = "Player 1 wins the battle";
				imgWinPlayer1.setVisibility(View.VISIBLE);
			} else if (battleResult == BattleResult.PLAYER_2_WIN) {
				result = "Player 2 wins the battle";
				imgWinPlayer2.setVisibility(View.VISIBLE);
			}
			txtInfo.setText(result);

			// cycle next starting army
			if (startingArmy == StartingArmyType.CAVALRY_VS_ARCHER) {
				startingArmy = StartingArmyType.MIX_ARMIES_VS_INFANTRY;
			} else {
				startingArmy = StartingArmyType.CAVALRY_VS_ARCHER;
			}
			break;
		}

		// match the image of player with its castle skin
		imgPlayer1.setImageResource(getCastleImage(game.getPlayer1Castle()));
		imgPlayer2.setImageResource(getCastleImage(game.getPlayer2Castle()));
		btnCastle1.setText(game.getPlayer1Castle().toString());
		btnCastle2.setText(game.getPlayer2Castle().toString());
	}

	private @DrawableRes int getCastleImage(Castle castle) {
		switch (castle.getBoostArmyType()) {
			case Archer: return R.drawable.archer;
			case Cavalry: return R.drawable.cavalry;
			case Infantry: return R.drawable.infantry;
			default: return R.drawable.catapult;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnBattle) { // if button is clicked
			// move the game to next game state
			game.nextState();
			// update everything
		} else if (v == btnCastle1) {
			game.cyclePlayer1Castle();
		} else if (v == btnCastle2) {
			game.cyclePlayer2Castle();
		}
		refresh();
	}
}
