package com.example.sriram.imagelist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 20;
    private static final int PIC_CROP = 2;
    View v;
    EditText entercap;
    TextView textView;
    private ImageView imgView;
    ArrayList<Bitmap> image = new ArrayList<Bitmap>();
    List<Uri> data1 = new ArrayList<>();

    //String[] caption = new String[500];
    List<String> caption = new ArrayList<String>();
    public int i=0;
    public int currimage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v=this.findViewById(android.R.id.content);
        imgView = (ImageView) findViewById(R.id.imageView);
        entercap   = (EditText)findViewById(R.id.editText2);
        textView = (TextView)findViewById(R.id.textView2);
    }

public void selectImgGal(View v)
{
Intent galleryIntent=new Intent(Intent.ACTION_PICK);
    File pictureDirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    String pictureDirectoryPath=pictureDirectory.getPath();
    Uri data=Uri.parse(pictureDirectoryPath);
    galleryIntent.setDataAndType(data,"image/*");

    startActivityForResult(galleryIntent, REQUEST_CODE);
}
public void showImage(View v)
{   currimage=0;
    if(i>0)
    {imgView.setImageBitmap(image.get(0));
    textView.setText ("Image 1 : "+caption.get(0));}
    else
    {
        i=0;
        imgView.setImageBitmap(null);
        textView.setText("");

    }
}
public void next(View v)
{
    currimage++;
    if(currimage==i)
        currimage=0;
    imgView.setImageBitmap(image.get(currimage));
    textView.setText("Image "+(currimage+1)+" : "+ caption.get(currimage));


}
public void cropImage(View v)
{
    try {Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(data1.get(currimage), "image/*");
        
        cropIntent.putExtra("crop", "true");
        
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        
        startActivityForResult(cropIntent, PIC_CROP);

    }
    catch(ActivityNotFoundException anfe){
        //display an error message
        String errorMessage = "Your device doesn't support the crop action!";
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}

public void delete(View v)
{
i--;
    caption.remove(currimage);
    image.remove(currimage);
    data1.remove(currimage);
    showImage(v);

}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE){
                 data1.add(data.getData());
                InputStream inputStream;
                try {
                    inputStream=getContentResolver().openInputStream(data1.get(i));
                    image.add(BitmapFactory.decodeStream(inputStream));
                    caption.add(entercap.getText().toString());
                    i++;
                    showImage(v);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"unable to open image", Toast.LENGTH_LONG).show();
                }
            }
            else
                if(requestCode==PIC_CROP)
                {
                    Bundle extras = data.getExtras();

                    Bitmap croppedPic = extras.getParcelable("data");
                    image.set(currimage,croppedPic);
                    showImage(v);
                }
        }

    }
}
