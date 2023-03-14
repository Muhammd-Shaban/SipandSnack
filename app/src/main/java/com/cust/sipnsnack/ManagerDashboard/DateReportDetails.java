package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DateReportDetails extends AppCompatActivity {

    TextView dateTV, netSaleTV, specialsTV, pizzaTV, burgersTV, friesTV, snacksTV, chilledDrinksTV,
            seaFoodsTV, coffeesTV, totalQtyTV, salesDescTV, emptySalesTV;

    TextView specialsTV2, pizzaTV2, burgersTV2, friesTV2, snacksTV2, chilledDrinksTV2,
            seaFoodsTV2, coffeesTV2;

    String date, netSale, specials, pizza, burgers, fries, snacks, chilled_drinks, sea_foods,
            coffees;
    int sp, pi, bu, fr, sn, ch, se, co, total;
    Button closeBTN;
    ImageView download, back, expandSales, collapseSales;
    RelativeLayout graphRL, salesRL, salesRL2;

    SharedPreferences spr;


    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    // PIE CHART Variables
    PieChart pieChartGraph;
    PieData pieData;
    List<PieEntry> pieEntryList = new ArrayList<>();

    public static String getTodayDate() {
        Date date;
        DateFormat setDate;
        date = Calendar.getInstance().getTime();
        setDate = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = setDate.format(date);
        return current_date;
    }

    public static String getCurrentDay() {
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = day.format(d);
        return dayOfTheWeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_report_details);

        spr = getSharedPreferences("DailyReportPREFS", MODE_PRIVATE);

        salesRL = findViewById(R.id.salesMainLayout);
        salesRL2 = findViewById(R.id.salesMainLayout2);
        graphRL = findViewById(R.id.graphLayout);
        dateTV = findViewById(R.id.dateTV);
        netSaleTV = findViewById(R.id.saleRight);
        totalQtyTV = findViewById(R.id.soldRight);
        specialsTV = findViewById(R.id.specialsRight);
        pizzaTV = findViewById(R.id.pizzaRight);
        burgersTV = findViewById(R.id.burgersRight);
        friesTV = findViewById(R.id.friesRight);
        snacksTV = findViewById(R.id.snacksRight);
        seaFoodsTV = findViewById(R.id.seaFoodsRight);
        coffeesTV = findViewById(R.id.coffeesRight);
        chilledDrinksTV = findViewById(R.id.drinksRight);
        salesDescTV = findViewById(R.id.salesDescription);
        emptySalesTV = findViewById(R.id.saleEmpty);
        back = findViewById(R.id.backBTN);
        download = findViewById(R.id.downloadIV);
        expandSales = findViewById(R.id.expandSales);
        collapseSales = findViewById(R.id.collapseSales);
        closeBTN = findViewById(R.id.closeBTN);
        specialsTV2 = findViewById(R.id.specialsRight2);
        pizzaTV2 = findViewById(R.id.pizzaRight2);
        burgersTV2 = findViewById(R.id.burgersRight2);
        friesTV2 = findViewById(R.id.friesRight2);
        snacksTV2 = findViewById(R.id.snacksRight2);
        seaFoodsTV2 = findViewById(R.id.seaFoodsRight2);
        coffeesTV2 = findViewById(R.id.coffeesRight2);
        chilledDrinksTV2 = findViewById(R.id.drinksRight2);

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DayWiseReports.class));
                finish();
            }
        });


        expandSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandSales.setVisibility(View.GONE);
                collapseSales.setVisibility(View.VISIBLE);
                graphRL.setVisibility(View.VISIBLE);
                salesRL.setVisibility(View.VISIBLE);
                salesRL2.setVisibility(View.VISIBLE);

                Toast.makeText(DateReportDetails.this, "Report Generated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        collapseSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandSales.setVisibility(View.VISIBLE);
                collapseSales.setVisibility(View.GONE);
                graphRL.setVisibility(View.GONE);
                salesRL.setVisibility(View.GONE);
                salesRL2.setVisibility(View.GONE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DayWiseReports.class));
                finish();
            }
        });

        date = DatesReportAdapter.date;
        netSale = DatesReportAdapter.netSale;
        specials = DatesReportAdapter.special;
        pizza = DatesReportAdapter.pizza;
        burgers = DatesReportAdapter.burgers;
        fries = DatesReportAdapter.fries;
        snacks = DatesReportAdapter.snacks;
        chilled_drinks = DatesReportAdapter.chilledDrinks;
        sea_foods = DatesReportAdapter.seaFoods;
        coffees = DatesReportAdapter.coffees;

        sp = Integer.parseInt(specials);
        pi = Integer.parseInt(pizza);
        bu = Integer.parseInt(burgers);
        fr = Integer.parseInt(fries);
        sn = Integer.parseInt(snacks);
        ch = Integer.parseInt(chilled_drinks);
        se = Integer.parseInt(sea_foods);
        co = Integer.parseInt(coffees);

        total = sp + pi + bu + fr + sn + ch + se + co;

        dateTV.setText(date);
        netSaleTV.setText(netSale + " Rs.");
        totalQtyTV.setText(String.valueOf(total));
        specialsTV.setText(specials);
        pizzaTV.setText(pizza);
        burgersTV.setText(burgers);
        friesTV.setText(fries);
        snacksTV.setText(snacks);
        chilledDrinksTV.setText(chilled_drinks);
        seaFoodsTV.setText(sea_foods);
        coffeesTV.setText(coffees);

        specialsTV2.setText(DatesReportAdapter.special2 + " Rs.");
        pizzaTV2.setText(DatesReportAdapter.pizza2 + " Rs.");
        burgersTV2.setText(DatesReportAdapter.burgers2 + " Rs.");
        friesTV2.setText(DatesReportAdapter.fries2 + " Rs.");
        snacksTV2.setText(DatesReportAdapter.snacks2 + " Rs.");
        chilledDrinksTV2.setText(DatesReportAdapter.chilledDrinks2 + " Rs.");
        seaFoodsTV2.setText(DatesReportAdapter.seaFoods2 + " Rs.");
        coffeesTV2.setText(DatesReportAdapter.coffees2 + " Rs.");


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 60, 60, false);


        // ***** PIE CHART *****
        pieChartGraph = findViewById(R.id.pieChartGraph);
        pieChartGraph.setUsePercentValues(true);

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Categories");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieData = new PieData(pieDataSet);


        if (total == 0) {

            emptySalesTV.setVisibility(View.VISIBLE);
            pieDataSet.setColor(R.color.matteBlack);

        } else {

            if (sp > 0) {
                pieEntryList.add(new PieEntry(sp, "Specials"));
            }

            if (pi > 0) {
                pieEntryList.add(new PieEntry(pi, "Pizza"));
            }

            if (bu > 0) {
                pieEntryList.add(new PieEntry(bu, "Burgers"));
            }

            if (fr > 0) {
                pieEntryList.add(new PieEntry(fr, "Fries"));
            }

            if (sn > 0) {
                pieEntryList.add(new PieEntry(sn, "Snacks"));
            }

            if (ch > 0) {
                pieEntryList.add(new PieEntry(ch, "Chilled Drinks"));
            }

            if (se > 0) {
                pieEntryList.add(new PieEntry(se, "Sea Foods"));
            }

            if (co > 0) {
                pieEntryList.add(new PieEntry(co, "Coffees"));
            }

        }

        pieChartGraph.getDescription().setText("Categories");
        pieChartGraph.setEntryLabelTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(10.f);
        pieDataSet.setValueLinePart1Length(0.43f);
        pieDataSet.setValueLinePart2Length(.1f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChartGraph.setEntryLabelColor(R.color.matteBlack);
        pieChartGraph.setData(pieData);
        pieChartGraph.invalidate();


        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }


        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DayWiseReports.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setColor(ContextCompat.getColor(this, R.color.black));

        Paint main_title = new Paint();
        main_title.setColor(ContextCompat.getColor(this, R.color.black));
        main_title.setTextSize(24);
        main_title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        main_title.setFlags(Paint.UNDERLINE_TEXT_FLAG);

        canvas.drawText("SIP N SNACK", 150, 60, title);
        canvas.drawText("TARAMMARI CHOWK", 150, 90, title);


        canvas.drawText("Generated Date : ", 570, 60, title);
        canvas.drawText(getTodayDate(), 680, 60, title);
        canvas.drawText("Generated Day : ", 570, 90, title);
        canvas.drawText(getCurrentDay(), 680, 90, title);

        String dt = DatesReportAdapter.date.replace('_', '/');

        canvas.drawText("Sales Report for the Date: " + dt, 220, 200, main_title);

        Paint title2 = new Paint();
        title2.setColor(ContextCompat.getColor(this, R.color.black));
        title2.setTextSize(14);
        title2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("Category", 290, 290, title2);
        canvas.drawText("Quantity", 390, 290, title2);
        canvas.drawText("Sales", 510, 290, title2);

        canvas.drawText("=============================================", 270, 305, title);

        canvas.drawText("Specials ", 290, 320, title);
        String specialsString = String.valueOf(DatesReportAdapter.special2 + " Rs.");
        canvas.drawText(specials, 390, 320, title);
        canvas.drawText(specialsString, 510, 320, title);

        canvas.drawText("Pizza ", 290, 340, title);
        String pizzaString = String.valueOf(DatesReportAdapter.pizza2 + " Rs.");
        canvas.drawText(pizza, 390, 340, title);
        canvas.drawText(pizzaString, 510, 340, title);

        canvas.drawText("Burgers ", 290, 360, title);
        String burgerString = String.valueOf(DatesReportAdapter.burgers2 + " Rs.");
        canvas.drawText(burgers, 390, 360, title);
        canvas.drawText(burgerString, 510, 360, title);

        canvas.drawText("Fries ", 290, 380, title);
        String friesString = String.valueOf(DatesReportAdapter.fries2 + " Rs.");
        canvas.drawText(fries, 390, 380, title);
        canvas.drawText(friesString, 510, 380, title);

        canvas.drawText("Snacks ", 290, 400, title);
        String snacksString = String.valueOf(DatesReportAdapter.snacks2 + " Rs.");
        canvas.drawText(snacks, 390, 400, title);
        canvas.drawText(snacksString, 510, 400, title);

        canvas.drawText("Chilled Drinks ", 290, 420, title);
        String drinksString = String.valueOf(DatesReportAdapter.chilledDrinks2 + " Rs.");
        canvas.drawText(chilled_drinks, 390, 420, title);
        canvas.drawText(drinksString, 510, 420, title);

        canvas.drawText("Sea Foods ", 290, 440, title);
        String seaFoodsString = String.valueOf(DatesReportAdapter.seaFoods2 + " Rs.");
        canvas.drawText(sea_foods, 390, 440, title);
        canvas.drawText(seaFoodsString, 510, 440, title);

        canvas.drawText("Coffees ", 290, 460, title);
        String coffeesString = String.valueOf(DatesReportAdapter.coffees2 + " Rs.");
        canvas.drawText(coffees, 390, 460, title);
        canvas.drawText(coffeesString, 510, 460, title);


        canvas.drawText("=============================================", 270, 490, title);


        canvas.drawText("Total", 290, 510, title2);
        String qtyString=String.valueOf(totalQtyTV.getText().toString());
        String saleString=String.valueOf(DatesReportAdapter.netSale + " Rs.");
        canvas.drawText(qtyString, 390, 510, title);
        canvas.drawText(saleString, 510, 510, title);

        canvas.drawText("Signature __________________", 570, 740, title);


        canvas.drawText("SIP N SNACK", 350, 940, title);
        canvas.drawText("HEALTHY FOOD & BEST TASTE !!", 310, 960, title);

        canvas.drawText("********************************************************", 250, 985, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));


        title.setTextSize(14);
        title.setTextAlign(Paint.Align.CENTER);

        pdfDocument.finishPage(myPage);

        try {
            pdfDocument.writeTo(new FileOutputStream(getPath(DatesReportAdapter.date)));

            Toast.makeText(DateReportDetails.this, "PDF File Generated Successfully.", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendNotification();
            }
        } catch (IOException e) {
            e.printStackTrace();

            SharedPreferences.Editor sprEdit = spr.edit();
            sprEdit.putString("Count", "1");

            String cont = spr.getString("Count", "");

            if(cont.equals("") || cont.equals(null)) {
                sprEdit.putString("Count", "1");
                generatePDF2("1");
            } else {
                generatePDF2(cont);
            }
            Toast.makeText(DateReportDetails.this, "Generated Successfully", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }


    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(String fileName) {
        File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(fileDirectory, fileName + "_Report.pdf");
        return file.getPath();
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePDF2(String cnt) {

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setColor(ContextCompat.getColor(this, R.color.black));

        Paint main_title = new Paint();
        main_title.setColor(ContextCompat.getColor(this, R.color.black));
        main_title.setTextSize(24);
        main_title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        main_title.setFlags(Paint.UNDERLINE_TEXT_FLAG);

        canvas.drawText("SIP N SNACK", 150, 60, title);
        canvas.drawText("TARAMMARI CHOWK", 150, 90, title);

        canvas.drawText("Generated Date : ", 570, 60, title);
        canvas.drawText(getTodayDate(), 680, 60, title);
        canvas.drawText("Generated Day : ", 570, 90, title);
        canvas.drawText(getCurrentDay(), 680, 90, title);

        String dt = DatesReportAdapter.date.replace('_', '/');


        canvas.drawText("Sales Report for the Date: " + dt, 220, 200, main_title);


        Paint title2 = new Paint();
        title2.setColor(ContextCompat.getColor(this, R.color.black));
        title2.setTextSize(14);
        title2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("Category", 290, 290, title2);
        canvas.drawText("Quantity", 390, 290, title2);
        canvas.drawText("Sales", 510, 290, title2);

        canvas.drawText("=============================================", 270, 305, title);

        canvas.drawText("Specials ", 290, 320, title);
        String specialsString = String.valueOf(DatesReportAdapter.special2 + " Rs.");
        canvas.drawText(specials, 390, 320, title);
        canvas.drawText(specialsString, 510, 320, title);

        canvas.drawText("Pizza ", 290, 340, title);
        String pizzaString = String.valueOf(DatesReportAdapter.pizza2 + " Rs.");
        canvas.drawText(pizza, 390, 340, title);
        canvas.drawText(pizzaString, 510, 340, title);

        canvas.drawText("Burgers ", 290, 360, title);
        String burgerString = String.valueOf(DatesReportAdapter.burgers2 + " Rs.");
        canvas.drawText(burgers, 390, 360, title);
        canvas.drawText(burgerString, 510, 360, title);

        canvas.drawText("Fries ", 290, 380, title);
        String friesString = String.valueOf(DatesReportAdapter.fries2 + " Rs.");
        canvas.drawText(fries, 390, 380, title);
        canvas.drawText(friesString, 510, 380, title);

        canvas.drawText("Snacks ", 290, 400, title);
        String snacksString = String.valueOf(DatesReportAdapter.snacks2 + " Rs.");
        canvas.drawText(snacks, 390, 400, title);
        canvas.drawText(snacksString, 510, 400, title);

        canvas.drawText("Chilled Drinks ", 290, 420, title);
        String drinksString = String.valueOf(DatesReportAdapter.chilledDrinks2 + " Rs.");
        canvas.drawText(chilled_drinks, 390, 420, title);
        canvas.drawText(drinksString, 510, 420, title);

        canvas.drawText("Sea Foods ", 290, 440, title);
        String seaFoodsString = String.valueOf(DatesReportAdapter.seaFoods2 + " Rs.");
        canvas.drawText(sea_foods, 390, 440, title);
        canvas.drawText(seaFoodsString, 510, 440, title);

        canvas.drawText("Coffees ", 290, 460, title);
        String coffeesString = String.valueOf(DatesReportAdapter.coffees2 + " Rs.");
        canvas.drawText(coffees, 390, 460, title);
        canvas.drawText(coffeesString, 510, 460, title);


        canvas.drawText("=============================================", 270, 490, title);


        canvas.drawText("Total", 290, 510, title2);
        String qtyString=String.valueOf(totalQtyTV.getText().toString());
        String saleString=String.valueOf(DatesReportAdapter.netSale + " Rs.");
        canvas.drawText(qtyString, 390, 510, title);
        canvas.drawText(saleString, 510, 510, title);



        canvas.drawText("Signature __________________", 570, 740, title);


        canvas.drawText("SIP N SNACK", 350, 940, title);
        canvas.drawText("HEALTHY FOOD & BEST TASTE !!", 310, 960, title);

        canvas.drawText("********************************************************", 250, 990, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));


        title.setTextSize(14);
        title.setTextAlign(Paint.Align.CENTER);
        pdfDocument.finishPage(myPage);

        try {

            pdfDocument.writeTo(new FileOutputStream(getPath(DatesReportAdapter.date+"("+cnt+")")));

            SharedPreferences.Editor editor = spr.edit();
            int x = Integer.parseInt(cnt);
            x+=1;
            editor.putString("Count", String.valueOf(x));
            editor.apply();


        } catch (IOException e) {
            e.printStackTrace();

        }
        pdfDocument.close();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotification() {

        String textTitle = "PDF GENERATED !";
        String textContent = "Dear Manager, PDF file has been Generated.";

        // Creating a notification channel
        NotificationChannel channel = new NotificationChannel("channel1", "hello", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        // Creating the notification object
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),"channel1");

        // Notification.setAutoCancel(true);
        notification.setContentTitle(textTitle);
        notification.setContentText(textContent);
        notification.setSmallIcon(R.drawable.cafe_main_logo);

        // Make the notification manager to issue a notification on the notification's channel
        manager.notify(121,notification.build());

    }
}