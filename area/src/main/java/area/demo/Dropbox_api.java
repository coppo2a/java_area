package area.demo;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class Dropbox_api {

    private String token;
    private String image_uploaded;
    private String image_uploaded_ext;

    Dropbox_api(String token, String image_uploaded_ext, String image_uploaded) throws DbxException {
        this.token = token;
        this.image_uploaded = image_uploaded;
        this.image_uploaded_ext = image_uploaded_ext;
        apiCore();
    }

    private void apiCore() throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, this.token);
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
        //try (InputStream in = new FileInputStream("3134-11.jpg")) {
            //FileMetadata metadata = client.files().uploadBuilder("/3134-11.jpg")
        System.out.println("path image_uploaded = " + image_uploaded);
        try (InputStream in = new FileInputStream(image_uploaded_ext)) {
            FileMetadata metadata = client.files().uploadBuilder("/" + image_uploaded)
                    .uploadAndFinish(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
