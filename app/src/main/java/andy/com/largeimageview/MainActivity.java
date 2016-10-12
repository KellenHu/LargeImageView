package andy.com.largeimageview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * 三张imageView
     * @param savedInstanceState
     */
    ImageView imageView1;
    ImageView imageView2;
    //大图
    LargeImageView largeImageView;


    /**
     * 图片分割工具类
     */
    BitmapRegionDecoder bitmapRegionDecoder;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView)findViewById(R.id.image2);
        largeImageView = (LargeImageView) findViewById(R.id.largeImageView);
        /**
         * 第二张是正常的填充的图片
         */
        imageView2.setImageResource(R.drawable.girl);
        /**
         * 第一张是截取图片的中间部分填充的图片填充的图片
         */
        try {
            InputStream inputStream = null;

            inputStream = getAssets().open("world201401.jpg");
        //* 获得图片的宽高

            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(inputStream, null, tmpOptions);
            int width = tmpOptions.outWidth;
            int height = tmpOptions.outHeight;
            Log.v("MainActivity", "计算所得当前图片girl.jpg的宽高分别为：width = " + width + "\t\t height = " + tmpOptions.outHeight);

            // * 设置显示图片的中心区域


            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(width/2 - 500, height/2 -300, width/2+500, height/2+300), options);

//            imageView1.setImageBitmap(bitmap);
            largeImageView.setInputStream(inputStream);



        }catch (Exception e){
            e.printStackTrace();
            Log.e("MainActivity",e.getMessage());
        }
        /**
         * 第三张是通过LargeImageView展示的超级大图世界地图
         */
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 无视结果,呲饭
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest .xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera){
            // Handle the camera action
        } else if (id == R.id.nav_gallery){
        } else if (id == R.id.nav_slideshow){
        } else if (id == R.id.nav_manage){
        } else if (id == R.id.nav_share){
        } else if (id == R.id.nav_send){
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}