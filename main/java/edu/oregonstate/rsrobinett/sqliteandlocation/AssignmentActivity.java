package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class AssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Spanned assignment_html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            assignment_html = Html.fromHtml(getString(R.string.assignment),Html.FROM_HTML_MODE_COMPACT );
        } else {
            assignment_html = Html.fromHtml(getString(R.string.assignment));
        }

        TextView view = (TextView) findViewById(R.id.text_view_assignment);
        view.setText(assignment_html);
    }
}
