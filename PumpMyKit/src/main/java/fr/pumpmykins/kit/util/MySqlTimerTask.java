/*package fr.pumpmykins.kit.util;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import fr.pumpmykins.kit.MainKit;


public class MySqlTimerTask {
		
	
	Timer timer;
	
	public MySqlTimerTask() {
		
		timer = new Timer();
		timer.schedule(new RefreshTask(), 0, 30*60000);
	}
	
	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			
			try {
				MainKit.getMySQL().refreshConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
*/