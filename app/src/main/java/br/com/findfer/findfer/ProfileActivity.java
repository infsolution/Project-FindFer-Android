package br.com.findfer.findfer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

import static android.R.attr.bitmap;
import static android.R.attr.id;

public class ProfileActivity extends AppCompatActivity implements Transaction{
    private SimpleDraweeView  imgProfile;
    private TextView userName, market, dateTime;
    private ImageButton ibtRelation;
    private Bundle intent;
    private String mediaProfile, url, imageName, encod;
    private long idUserPoster;
    private User user;
    private Uri uriImage;
    private File fileImageUser;
    private Bitmap bitmap;
    private ProgressBar pbLoadRelation;
    private boolean statusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);
        user = getUser();
        intent = getIntent().getExtras();
        idUserPoster = intent.getLong("id_marketer");
        statusButton = intent.getBoolean("status_request_relationship");
        imgProfile =(SimpleDraweeView)findViewById(R.id.img_profile);
        userName = (TextView)findViewById(R.id.tv_name_user);
        market = (TextView)findViewById(R.id.tv_market_name);
        dateTime = (TextView)findViewById(R.id.tv_date_poster);
        ibtRelation = (ImageButton)findViewById(R.id.imb_add_relationship);
        ibtRelation.setClickable(statusButton);
        pbLoadRelation = (ProgressBar)findViewById(R.id.pb_load_relationship);
        mediaProfile = intent.getString("media_profile");
        url = "http://www.findfer.com.br/FindFer/control/RequestRelationShip.php";
        loadData();
        setButtonNoClick(user.getCodUser(), idUserPoster);
    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    /*public void loadData(){
        userName.setText(intent.getString("name_user"));
        market.setText(intent.getString("name_market"));
        dateTime.setText(intent.getString("date_time"));
        loadImage(mediaProfile);
    }*/

    public void requestRelation(View view){
                callVolleyRequest();
    }
    public void setButtonNoClick(long idClient, long idMarketer){
        if(idClient == idMarketer){
            ibtRelation.setClickable(false);
        }
    }
    public void loadData(){
        userName.setText(intent.getString("name_user"));
        market.setText(intent.getString("name_market"));
        dateTime.setText(intent.getString("date_time"));
        loadImage(mediaProfile);
    }
    private void loadImage(String mediaPath){
        Uri uri = Uri.parse(mediaPath);
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(imgProfile.getController())
                .build();
        imgProfile.setController(dc);
    }
    /*public void updateImage(View view){
        Intent openCard = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //openCard.putExtra(MediaStore.EXTRA_OUTPUT, fileImageUser);
        startActivityForResult( openCard,0);
        //startActivityForResult(Intent.createChooser(openCard, "IMAGE_SELECTED") ,101);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == 0 && resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                //mediaProfile = bundle.getString("");
                Uri path = data.getData();
                String media = path.toString();
                loadImage(media);
            /*
            bitmap =(Bitmap) bundle.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] array = stream.toByteArray();
            encod = Base64.encodeToString(array, 0);
        }

    }

    private void loadImage(String mediaPath){
        Uri uri = Uri.parse(mediaProfile);
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(imgProfile.getController())
                .build();
        imgProfile.setController(dc);
    }*/
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }


    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public Map<String, String> doBefore() {
        pbLoadRelation.setVisibility(View.VISIBLE);
        ibtRelation.setVisibility(View.GONE);
        //Log.i("MYLOG","Abriu o doBefore profileActivity");
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_client",Long.toString(user.getCodUser()));
            parameters.put("id_marketer",Long.toString(idUserPoster));
            parameters.put("type_account",Integer.toString(user.getTypeAccount()));
            return parameters;
        }else{
            Toast.makeText(this, "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        pbLoadRelation.setVisibility(View.GONE);
        ibtRelation.setVisibility(View.VISIBLE);
        long resp = createRelationship(response);
        if(resp > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle(R.string.dial_title_request_relationShip);
            builder.setMessage(R.string.dial_message_request_relationship_successful);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ibtRelation.setClickable(false);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(resp == -1){
            ibtRelation.setClickable(false);
            Toast.makeText(this, "Opa! Você já é cliente deste feirante!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Opa! Houve um erro ao processar sua solicitação!", Toast.LENGTH_SHORT).show();
        }
    }
    public long createRelationship(String response){
        long result = -5;
        if(response != null){
            try {
                JSONArray relation = new JSONArray(response);
                JSONObject relationship = relation.getJSONObject(0);
                result = relationship.getInt("retorno");
            } catch (JSONException e) {
                Toast.makeText(this, "Opa! Houve um erro ao processar sua solicitação!", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }
}
