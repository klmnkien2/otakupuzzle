package dovietkien.vn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

/**
 * Profile operations.
 */
public class ProfileService
{
    // the location of the profile data file
    private static final String PROFILE_DATA_FILE = ".xephinh/profile-v1.json";

    // the loaded profile (may be null)
    private Profile profile;

    /**
     * Creates the profile service.
     */
    public ProfileService()
    {
    }

    /**
     * Retrieves the player's profile, creating one if needed.
     */
    public Profile retrieveProfile()
    {
        // if the profile is already loaded, just return it
        if( profile != null ) return profile;

        // create the handle for the profile data file
        FileHandle profileDataFile = Gdx.files.external( PROFILE_DATA_FILE );

        // create the JSON utility object
        Json json = new Json();

        // check if the profile data file exists
        if( profileDataFile.exists() ) {

            // load the profile from the data file
            try {

                // read the file as text
                String profileAsCode = profileDataFile.readString();

                // decode the contents
                String profileAsText = Base64Coder.decodeString( profileAsCode );

                // restore the state
                profile = json.fromJson( Profile.class, profileAsText );

            } catch( Exception e ) {
                profile = new Profile();
                persist( profile );

            }

        } else {
            profile = new Profile();
            persist( profile );
        }

        return profile;
    }

    /**
     * Persists the given profile.
     */
    protected void persist(Profile profile )
    {
        // create the JSON utility object
        Json json = new Json();

        // create the handle for the profile data file
        FileHandle profileDataFile = Gdx.files.external( PROFILE_DATA_FILE );

        // convert the given profile to text
        String profileAsText = json.toJson( profile );

        // encode the text
        String profileAsCode = Base64Coder.encodeString( profileAsText );

        // write the profile data file
        profileDataFile.writeString( profileAsCode, false );
    }

    /**
     * Persists the player's profile.
     * <p>
     * If no profile is available, this method does nothing.
     */
    public void persist()
    {
        if( profile != null ) {
            persist( profile );
        }
    }
}
