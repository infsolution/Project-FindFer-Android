package br.com.findfer.findfer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Map;

import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.Transaction;

public class ProfileEditActivity extends AppCompatActivity implements Transaction {
    private SimpleDraweeView imgProfile;
    private TextView userName, market, dateTime, codUser, email, fone, latitude, longitude, type;
    private Button btRelation;
    private Bundle intent;
    private String mediaProfile, url, imageName, encod;
    private long idUserRel;
    private User user;
    private Uri uriImage;
    private File fileImageUser;
    private Bitmap bitmap;
    ImageButton imbUpdateImaage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile_edit);
        user = getUser();
        //intent = getIntent().getExtras();
        imbUpdateImaage =(ImageButton)findViewById(R.id.imb_update_image);
        imgProfile =(SimpleDraweeView)findViewById(R.id.img_profile);
        userName = (TextView)findViewById(R.id.tv_name_user);
        market = (TextView)findViewById(R.id.tv_market_name);
        dateTime = (TextView)findViewById(R.id.tv_date_poster);
        codUser = (TextView) findViewById(R.id.tv_cod_usesr);
        email = (TextView)findViewById(R.id.tv_email);
        fone = (TextView)findViewById(R.id.tv_fone);
        longitude = (TextView)findViewById(R.id.tv_local_longitude);
        latitude = (TextView)findViewById(R.id.tv_local_latitude);
        type = (TextView)findViewById(R.id.tv_type_account);
        mediaProfile = user.getImage();
        url = "";
        loadData();
    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    public void loadData(){
        userName.setText(user.getName());
        //market.setText(intent.getString("name_market"));
        dateTime.setText(user.getDateRegister());
        codUser.setText(Long.toString(user.getCodUser()));
        email.setText(user.getEmail());
        fone.setText(user.getFone());
        latitude.setText(Double.toString(user.getActualLatitude()));
        longitude.setText(Double.toString(user.getActualLongitude()));
        type.setText(Integer.toString(user.getTypeAccount()));
        loadImage(mediaProfile, imgProfile);
    }
    public void updateImage(View view){
        Intent openCard = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult( openCard,0);
        //startActivityForResult(Intent.createChooser(openCard, "IMAGE_SELECTED") ,101);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
           Uri imageUri = data.getData();
            String [] colunFile = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, colunFile, null,null,null);
            cursor.moveToFirst();
            int colunIndex = cursor.getColumnIndex(colunFile[0]);
            String imagePath = cursor.getString(colunIndex);
            Bitmap imageReturned = BitmapFactory.decodeFile(imagePath);
            if(imageReturned != null){
                loadImage(imagePath, imgProfile);
            }
        }
    }
    public void getFileUri(){
        imageName = setImageName();
        fileImageUser = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator + imageName);
        uriImage = Uri.fromFile(fileImageUser);
    }
    private String setImageName() {
        return user.getName()+user.getFone()+".jpg";
    }
    private String formatDate(String date){
        return date.substring(8,10)+"/"+date.substring(5,7)+"/"+date.substring(0,4)+" "+date.substring(12,19);
    }

    private void loadImage(String mediaPath, SimpleDraweeView fileImage){
        Uri uri = Uri.parse(mediaPath);
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(fileImage.getController())
                .build();
        fileImage.setController(dc);
    }
    @Override
    public Map<String, String> doBefore() {
        return null;
    }

    @Override
    public void doAfter(String response) {

    }
}
