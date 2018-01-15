package demo.material.pdfviewer;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class MainActivity extends AppCompatActivity implements DownloadFile.Listener {

    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    EditText etPdfUrl;
    Button btnDownload;
    PDFPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Hello World");
        setContentView(R.layout.activity_main);

        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        etPdfUrl = (EditText) findViewById(R.id.et_pdfUrl);
        btnDownload = (Button) findViewById(R.id.btn_download);

        setDownloadButtonListener();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remotePDFViewPager = new RemotePDFViewPager(ctx, getUrlFromEditText(), listener);
                remotePDFViewPager.setId(R.id.pdfViewPager);
                hideDownloadButton();
            }
        });
    }

    protected String getUrlFromEditText() {
        //return "https://drive.google.com/uc?id=1xEuUo4XEqPqZzqSG_SSNQULErl2rlH3i&export=download";
        return "https://drive.google.com/uc?id=191RwUjk92LDp8ATQay1qT8OwUzXp1TAM&export=download";
    }

    public static void open(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    public void showDownloadButton() {
        btnDownload.setVisibility(View.VISIBLE);
    }

    public void hideDownloadButton() {
        btnDownload.setVisibility(View.INVISIBLE);
    }

    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(etPdfUrl,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(btnDownload,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        showDownloadButton();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        showDownloadButton();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}
