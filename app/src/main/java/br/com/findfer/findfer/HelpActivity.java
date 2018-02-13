package br.com.findfer.findfer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    private TextView faq;
    private TextView feedback;
    private TextView terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        faq = (TextView)findViewById(R.id.tv_faq);
        feedback = (TextView)findViewById(R.id.tv_feedback);
        terms = (TextView)findViewById(R.id.tv_terms);
    }

    public void sendFeedBack(View view){
        Intent goFeedback = new Intent(this, FeedbackActivity.class);
        startActivity(goFeedback);
    }

    public void goFaq(View view){
        Intent faq = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.findfer.com.br/faq.php")) ;
        startActivity ( faq ) ;
    }
    public void goTerms(View view){
        Intent terms = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.findfer.com.br/terms.php"));
        startActivity(terms);
    }
}
