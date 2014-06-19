package dovietkien.vn;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.OrderedMap;

public class Profile implements Serializable
{
    private Map<String,String> highScores;
    public static int number = 5;

    public Profile()
    {
        highScores = new HashMap<String,String>();
    }
    
    public Map<String,String> getHighScores()
    {
        return highScores;
    }

    /**
     * Gets the current high score for the given level.
     */
    public Array<String> getHighScore(String levelId)
    {
        if( highScores == null ) return null;
        Array<String> lst = new Array<String>();
        for(int i=1; i<=number; i++) {
        	String tmp = highScores.get(levelId + "." + i);
        	lst.add(tmp == null ? "00:00" : tmp);
        }
        
        return lst;
    }

    /**
     * Notifies the score on the given level. Returns <code>true</code> if its a
     * high score.
     */
    public boolean notifyScore(String levelId, String score)
    {
    	Array<String> lst = getHighScore( levelId );
    	int i=number-1; 
    	while( Integer.parseInt(score.replaceAll(":", "")) > Integer.parseInt(lst.get(i).replaceAll(":", "")) && i>0) {
            i--;
    	}
    	lst.insert(i, score);
        
    	for(int j=0; j<number; j++) {
        	highScores.put(levelId + "." + (j+1), lst.get(j));
        }
        return false;
    }
    
    @SuppressWarnings( "unchecked" )
    @Override
    public void read(Json json, OrderedMap<String,Object> jsonData)
    {
        Map<String,String> highScores = json.readValue( "highScores", HashMap.class,
        		String.class, jsonData );
        for( String levelIdAsString : highScores.keySet() ) {
        	String levelId = levelIdAsString;
            String highScore = highScores.get( levelIdAsString );
            this.highScores.put( levelId, highScore );
        }
    }

    @Override
    public void write(Json json )
    {
        json.writeValue( "highScores", highScores );
    }
}
