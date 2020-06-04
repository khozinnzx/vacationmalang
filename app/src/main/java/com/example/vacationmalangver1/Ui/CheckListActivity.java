package com.example.vacationmalangver1.Ui;

import android.app.Dialog;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationmalangver1.Model.JarakAntarLok;
import com.example.vacationmalangver1.Model.LokasiUser;
import com.example.vacationmalangver1.Model.LokasiWisata;
import com.example.vacationmalangver1.Model.UrutanWisata;
import com.example.vacationmalangver1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.GeoApiContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class CheckListActivity extends AppCompatActivity {

    private static int[][] tempData;
    private static double[] tempJarak;
    double[][] matrix;
    public double[][] matrixAkhir;

    ArrayList<Integer> selectedValueList = new ArrayList<Integer>();
    String[] node;
    LokasiUser mLokasiUser;
    LokasiWisata lokasiWisata;
    ArrayList<Float> jarakAwalList = new ArrayList<Float>();
//    float result[][];
//    float current;

    private static final String TAG = "CheckListActivity";
    private ArrayList<JarakAntarLok> jarakList = new ArrayList<>();
    JarakAntarLok mJarakAntarLok;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference jarakLokRef = mDb.collection("jarak lokasi");
    private CollectionReference lokasiWisataRef = mDb.collection("lokasi wisata");
    private GeoApiContext mGeoApiContext = null;

    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9,
            checkBox10, checkBox11, checkBox12, checkBox13, checkBox14, checkBox15;
    Button btn_clear, btn_urut;

    //Integer[] node = selectedValueList.toArray(new Integer[selectedValueList.size()]);
    Integer[][] tujuan;
    float jarakTotal;
    String[] jalur;
    float[] current;
    float[] jarak_kemungkinan;
    String jarak_kemungkinan_sama;
    int terpilih;
    int[] terpilihArr;

    double node_current[];
    ArrayList<String> namaWisataUrut = new ArrayList<>();
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        checkBox8 = findViewById(R.id.checkBox8);
        checkBox9 = findViewById(R.id.checkBox9);
        checkBox10 = findViewById(R.id.checkBox10);
        checkBox11 = findViewById(R.id.checkBox11);
        checkBox12 = findViewById(R.id.checkBox12);
        checkBox13 = findViewById(R.id.checkBox13);
        checkBox14 = findViewById(R.id.checkBox14);
        checkBox15 = findViewById(R.id.checkBox15);
        btn_urut = findViewById(R.id.btn_urutan);
        mProgressBar = findViewById(R.id.progressBar);


        getLokasiUser();

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
        }


        btn_urut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                btn_urut.setVisibility(View.INVISIBLE);
                getAllJarak();


                //Log.d(TAG, "matrixakhirget ; " + Arrays.deepToString(currents));

                //getUrutan();
                Toast.makeText(getApplicationContext(), selectedValueList.toString() + "\n", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    public void getUrutan() {

        Log.d(TAG, "matrix akhir coba2 : " + Arrays.deepToString(matrixAkhir));
        node = new String[selectedValueList.size()];
        for (int i = 0; i < selectedValueList.size(); i++) {
            node[i] = String.valueOf(selectedValueList.get(i));
        }
        Log.d(TAG, "node = " + Arrays.toString(node));
        Log.d(TAG, "current = " + Arrays.toString(current));


        Log.d(TAG, "current ==" + Arrays.toString(node_current));
        int panjang = getPanjang(selectedValueList.size());
        jalur = new String[panjang];
        runProgram();

        final Dialog dialog = new Dialog(CheckListActivity.this);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog.setTitle("Hasil Perbandingan");

        TextView text1 = dialog.findViewById(R.id.tv_1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Menentukan Kemungkinan\n===============\n");
        for (int i = 0; i < jalur.length; i++) {
            stringBuilder.append("Kemungkinan ke-" + i + "=>" + jalur[i] + "\n");
        }
        stringBuilder.append("======================\n");
        stringBuilder.append("Menentukan Panjang Jarak\n=============\n");
        jarak_kemungkinan = new float[jalur.length];
        run_jarak_kemungkinan();
        for (int i = 0; i < jarak_kemungkinan.length; i++) {
            stringBuilder.append("Kemungkinan Ke-" + i + " => " + jarak_kemungkinan[i] +" km"+ "\n");
        }
        stringBuilder.append("======================\n");
        float terpendek = (float) cariTerpendek();
        stringBuilder.append("Jarak Terpendek =>" + terpendek+" km"+"\n");
        stringBuilder.append("Jalur Terpendek\n");
        String pecah_terpendek[] = jarak_kemungkinan_sama.split("-");
        terpilih= Integer.parseInt(pecah_terpendek[0]);
        terpilihArr = new int[node.length];
        Log.d(TAG, "jarak kemungkinan sama : " + jarak_kemungkinan_sama);
        for (int i = 0; i < pecah_terpendek.length; i++) {
            stringBuilder.append("Kemungkinan ke-" + pecah_terpendek[i]);
            stringBuilder.append("===>" + jalur[Integer.parseInt(pecah_terpendek[i])]);

        }
        String pilihan[] =jalur[Integer.parseInt(pecah_terpendek[0])].split("-");
        for (int i=0; i<terpilihArr.length;i++){
            terpilihArr[i] = Integer.parseInt(pilihan[i]);
        }
        Log.d(TAG, "terpilih : "+Arrays.toString(terpilihArr));
        lokasiWisataRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int m=0; m<terpilihArr.length; m++ ){
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        lokasiWisata = documentSnapshot.toObject(LokasiWisata.class);
                        if (terpilihArr[m] == lokasiWisata.getId_wisata()){
                            namaWisataUrut.add(lokasiWisata.getNama_wisata());
                        }
                    }
                }
            }
        });
        Log.d(TAG, "getUrutan: "+namaWisataUrut.toString());

        text1.setText(stringBuilder);
        Button dialogBtnOk = dialog.findViewById(R.id.btn_ok);
        Button dialogBtnCancel = dialog.findViewById(R.id.btn_cancel);
        dialog.show();
        hideDialog();
        btn_urut.setVisibility(View.VISIBLE);
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("intent_list",namaWisataUrut);
                    MapFragment mapFragment = new MapFragment();
                    mapFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container,mapFragment)
                            .commit();
                    hideDialog();
                    dialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }


    public int getPanjang(int pnode) {
        int panjang = 1;
        for (double i = pnode; i >= 1; i--) {
            panjang *= i;
        }
        Log.d(TAG, "getPanjang: " + panjang);
        return panjang;
    }

    public String acak_urutan() {
        String temp_urut[] = new String[node.length];
        String noArr = "";
        int i = 0;
        Random r = new Random();
        while (i < node.length) {
            int angka = r.nextInt(node.length);
            boolean stat_ang = true;
            for (int j = 0; j < temp_urut.length; j++) {
                if (temp_urut[j]==node[angka]) {
                    stat_ang = false;
                    break;
                }
            }
            if (stat_ang) {
                temp_urut[i] = node[angka];
                i++;
            }
        }
        for (int j = 0; j < node.length; j++) {
            noArr += temp_urut[j] + "-";
        }
        Log.d(TAG, "acak_urutan: " + noArr);
        return noArr;
    }

    public boolean olahJalur(int ur) {
        boolean hasil = true;
        String getUrutan = acak_urutan();
        for (int i = 0; i < jalur.length; i++) {
            if (getUrutan.equals(jalur[i])) {
                hasil = false;
                break;
            }
        }
        if (hasil) {
            jalur[ur] = getUrutan;
        }
        Log.d(TAG, "olahJalur: " + hasil);
        return hasil;
    }

    void runProgram() {
        int i = 0;
        while (i < jalur.length) {
            if (olahJalur(i)) {
                i++;
            }
        }
    }


    void run_jarak_kemungkinan() {

        for (int i = 0; i < jalur.length; i++) {
            String kemungkinan[] = jalur[i].split("-");
            float panjang_jarak = 0;
            String node_sebelum = "";
            for (int j = 0; j < kemungkinan.length; j++) {
                panjang_jarak += getHitung(node_sebelum, kemungkinan[j]);
                node_sebelum = kemungkinan[j];
            }
            jarak_kemungkinan[i] = panjang_jarak;
            Log.d(TAG, "run_jarak_kemungkinan: " + Arrays.toString(jarak_kemungkinan));

        }
    }

    public float getHitung(String sebelum, String sekarang) {
        Log.d(TAG, "current di gethitung : " + Arrays.toString(current));
        Log.d(TAG, "jarak di gethitung : "+ Arrays.deepToString(matrixAkhir));

        float jarak_ke_node = 0;
        if (sebelum=="") {
            for (int i = 0; i < node.length; i++) {
                if (node[i].equals(sekarang)) {
                    jarak_ke_node = current[i];
                    Log.d(TAG, "jarak ke node get hitung : " + jarak_ke_node);
                }
            }

        } else {
            int x = 0;
            int y = 0;
            for (int i = 0; i < node.length; i++) {
                if (node[i].equals(sebelum)) {
                    x = i;
                    break;
                }
            }
            for (int i = 0; i < node.length; i++) {
                if (node[i].equals(sekarang)) {
                    y = i;
                    break;
                }
            }
            jarak_ke_node = (float) matrixAkhir[x][y];
        }
        Log.d(TAG, "getHitung: " + jarak_ke_node);
        return jarak_ke_node;
    }


    public double cariTerpendek() {
        double temp = jarak_kemungkinan[0];
        for (int i = 1; i < jarak_kemungkinan.length; i++) {
            if (temp > jarak_kemungkinan[i]) {
                temp = jarak_kemungkinan[i];
            }
        }
        for (int i = 1; i < jarak_kemungkinan.length; i++) {
            if (temp == jarak_kemungkinan[i]) {
                jarak_kemungkinan_sama = +i + "-";
            }
        }
        Log.d(TAG, "kemungkinan sama = " + jarak_kemungkinan_sama);
        Log.d(TAG, "cariTerpendek: " + temp);
        return temp;
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        String str = "";

        switch (view.getId()) {
            case R.id.checkBox1:
                str = checked ? "selected pantai balekambang" : "deselected pantai balekambang";
                if (checked) {
                    selectedValueList.add(1);
                } else {
                    selectedValueList.remove(Integer.valueOf(1));
                }
                break;
            case R.id.checkBox2:
                str = checked ? "selected sumber maron" : "deselected sumber maron";
                if (checked) {
                    selectedValueList.add(2);
                } else {
                    selectedValueList.remove(Integer.valueOf(2));
                }

                break;

            case R.id.checkBox3:
                str = checked ? "selected air terjun coban rondo" : "deselected air terjun coban rondo";
                if (checked) {
                    selectedValueList.add(3);
                } else {
                    selectedValueList.remove(Integer.valueOf(3));
                }

                break;
            case R.id.checkBox4:
                str = checked ? "selected jatim park 2" : "deselected jatim park 2";
                if (checked) {
                    selectedValueList.add(4);
                } else {
                    selectedValueList.remove(Integer.valueOf(4));
                }
                break;
            case R.id.checkBox5:
                str = checked ? "selected omah kayu" : "deselected omah kayu";
                if (checked) {
                    selectedValueList.add(5);
                } else {
                    selectedValueList.remove(Integer.valueOf(5));
                }
                break;
            case R.id.checkBox6:
                str = checked ? "selected museum angkut" : "deselected museum angkut";
                if (checked) {
                    selectedValueList.add(6);
                } else {
                    selectedValueList.remove(Integer.valueOf(6));
                }
                break;
            case R.id.checkBox7:
                str = checked ? "selected pantai tiga warna" : "deselected pantai tiga warna";
                if (checked) {
                    selectedValueList.add(7);
                } else {
                    selectedValueList.remove(Integer.valueOf(7));
                }
                break;
            case R.id.checkBox8:
                str = checked ? "selected pantai goa cina" : "deselected pantai goa cina";
                if (checked) {
                    selectedValueList.add(8);
                } else {
                    selectedValueList.remove(Integer.valueOf(8));
                }
                break;
            case R.id.checkBox9:
                str = checked ? "selected predator fun park" : "deselected predator fun park";
                if (checked) {
                    selectedValueList.add(9);
                } else {
                    selectedValueList.remove(Integer.valueOf(9));
                }

                break;
            case R.id.checkBox10:
                str = checked ? "selected wisata paralayang" : "deselected wisata paralayang";
                if (checked) {
                    selectedValueList.add(10);
                } else {
                    selectedValueList.remove(Integer.valueOf(10));
                }
                break;
            case R.id.checkBox11:
                str = checked ? "selected hawaii waterpark" : "deselected hawaii waterpark";
                if (checked) {
                    selectedValueList.add(11);
                } else {
                    selectedValueList.remove(Integer.valueOf(11));
                }
                break;
            case R.id.checkBox12:
                str = checked ? "selected batu night spectacular" : "deselected batu night spectacular";
                if (checked) {
                    selectedValueList.add(12);
                } else {
                    selectedValueList.remove(Integer.valueOf(12));
                }

                break;
            case R.id.checkBox13:
                str = checked ? "selected taman selecta" : "deselected taman selecta";
                if (checked) {
                    selectedValueList.add(13);
                } else {
                    selectedValueList.remove(Integer.valueOf(13));
                }

                break;
            case R.id.checkBox14:
                str = checked ? "selected banyu anjlok" : "deselected banyu anjlok";
                if (checked) {
                    selectedValueList.add(14);
                } else {
                    selectedValueList.remove(Integer.valueOf(14));
                }

                break;
            case R.id.checkBox15:
                str = checked ? "selected jodipan" : "deselected jodipan";
                if (checked) {
                    selectedValueList.add(15);
                } else {
                    selectedValueList.remove(Integer.valueOf(15));
                }

                break;
            case R.id.checkBox16:
                str = checked ? "selected jatim park 1" : "deselected jatim park 1";
                if (checked) {
                    selectedValueList.add(16);
                } else {
                    selectedValueList.remove(Integer.valueOf(16));
                }

                break;
            case R.id.checkBox17:
                str = checked ? "selected pantai ngliyep" : "deselected pantai ngliyep";
                if (checked) {
                    selectedValueList.add(17);
                } else {
                    selectedValueList.remove(Integer.valueOf(17));
                }

                break;
            case R.id.checkBox18:
                str = checked ? "selected batu wonderland" : "deselected batu wonderland";
                if (checked) {
                    selectedValueList.add(18);
                } else {
                    selectedValueList.remove(Integer.valueOf(18));
                }

                break;
            case R.id.checkBox19:
                str = checked ? "selected museum brawijaya" : "deselected museum brawijaya";
                if (checked) {
                    selectedValueList.add(19);
                } else {
                    selectedValueList.remove(Integer.valueOf(19));
                }

                break;
            case R.id.checkBox20:
                str = checked ? "selected coban talun" : "deselected coban talun";
                if (checked) {
                    selectedValueList.add(20);
                } else {
                    selectedValueList.remove(Integer.valueOf(20));
                }

                break;
        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


    public void ambiljarak() {
//        int[] input = {1, 2, 3};
        matrix = new double[selectedValueList.size()][selectedValueList.size()];
        for (int i = 0; i < selectedValueList.size(); i++) {
            for (int j = 0; j < selectedValueList.size(); j++) {
                matrix[i][j] = getJarak(selectedValueList.get(i), selectedValueList.get(j));
                Log.d(TAG, "jarak nya dari :" + selectedValueList.get(i) + " ke " + selectedValueList.get(j) + " adalah " + matrix[i][j]);
                Log.d(TAG, "matrix : " + Arrays.deepToString(matrix));

            }
        }
        floydWarshall(matrix);
        getAllLokasiWisata();

        //getUrutan();

        Log.d(TAG, "matrixAkhir : " + Arrays.deepToString(matrixAkhir));


    }

    private double getJarak(int lokAwal, int lokAkhir) {
        double hasil_jarak = 0;
        for (int k = 0; k < tempJarak.length; k++) {
            if (lokAwal == tempData[k][0] && lokAkhir == tempData[k][1]) {
                hasil_jarak = tempJarak[k];
            }
        }
        return hasil_jarak;
    }

    public void getAllJarak() {
        jarakLokRef
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    tempData = new int[queryDocumentSnapshots.size()][2];
                    tempJarak = new double[queryDocumentSnapshots.size()];
                    int count = 0;
                    for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                        JarakAntarLok jarak = documentSnapshots.toObject(JarakAntarLok.class);
                        tempData[count][0] = jarak.getLokasiAwal();
                        tempData[count][1] = jarak.getLokasiTujuan();
                        tempJarak[count] = jarak.getJarak();
                        count++;


    //                    Log.d(TAG, "onSuccess: "+queryDocumentSnapshots);
    //                    Log.d(TAG, "panjang :"+ queryDocumentSnapshots.size());
    //
    //                    Log.d(TAG, "jarak dari "+jarak.getLokasiAwal()+" ke "+jarak.getLokasiTujuan()+" adalah : "+jarak.getJarak());
                    }
                    ambiljarak();
                });
    }

    private void getLokasiUser() {

        DocumentReference userLocRef = mDb.collection("lokasi user")
                .document(FirebaseAuth.getInstance().getUid());
        userLocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: sukses dapatkan user lok");
                    mLokasiUser = task.getResult().toObject(LokasiUser.class);
                    Log.d(TAG, "onComplete: get lokasi dengan nama : " + mLokasiUser.getUser() + " : " + mLokasiUser.getGeo_point().getLatitude() + "," + mLokasiUser.getGeo_point().getLongitude());

                }
            }
        });
    }


    void floydWarshall(double matrix[][]) {
        int V = selectedValueList.size();
        matrixAkhir = new double[V][V];
        int i, j, k;

        for (i = 0; i < V; i++)
            for (j = 0; j < V; j++)
                matrixAkhir[i][j] = matrix[i][j];

        for (k = 0; k < V; k++) {
            // Pick all vertices as source one by one
            for (i = 0; i < V; i++) {
                // Pick all vertices as destination for the
                // above picked source
                for (j = 0; j < V; j++) {
                    // If vertex k is on the shortest path from
                    // i to j, then update the value of dist[i][j]
                    if (matrixAkhir[i][k] + matrixAkhir[k][j] < matrixAkhir[i][j])
                        matrixAkhir[i][j] = matrixAkhir[i][k] + matrixAkhir[k][j];
                }
            }
        }
//        matrixAkhir = dist;
//        for (int o=0; o<matrixAkhir.length; o++) {
//            for (int p =0; p<matrixAkhir.length; p++) {
//
//                Log.d(TAG, "floydWarshall: " + matrixAkhir[o][p]);
//            }
//        }
        // Print the shortest distance matrix
        // printSolution(matrixAkhir);
    }

    void printSolution(double dist[][]) {
        int V = selectedValueList.size();
        int INF = 9999;
        System.out.println("The following matrix shows the shortest " +
                "distances between every pair of vertices");
        Log.d(TAG, "The following matrix shows the shortest " +
                "distances between every pair of vertices");
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < V; ++j) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + "   ");
            }
            System.out.println();
        }
    }

    public void getAllLokasiWisata() {
        current = new float[selectedValueList.size()];
        node_current = new double[selectedValueList.size()];
        lokasiWisataRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //result = new float[selectedValueList.size()][2];
                for (int i = 0; i < selectedValueList.size(); i++) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        lokasiWisata = documentSnapshot.toObject(LokasiWisata.class);

                        if (selectedValueList.get(i) == lokasiWisata.getId_wisata()) {
                            Log.d(TAG, "hitungan " + i);
                            Log.d(TAG, "onSuccess: berhasil if selectedValue");
                            Log.d(TAG, "onSuccess: berhasil if selectedValue geopoint : " + lokasiWisata.getGeo_point_wisata());
//                            long jarak = calculateDirections(lokasiWisata.getGeo_point_wisata());
//                            Log.d(TAG, "onSuccess: berhasil if selectedValue long jarak :"+ jarak);

//                            jarakAwalList.add(calculateDirections(lokasiWisata.getGeo_point_wisata()));
//                            Log.d(TAG, "listJarak : " + jarakAwalList.get(i));
                            float hasilDistance[] = new float[10];
                            Location.distanceBetween(lokasiWisata.getGeo_point_wisata().getLatitude(), lokasiWisata.getGeo_point_wisata().getLongitude()
                                    , mLokasiUser.getGeo_point().getLatitude(), mLokasiUser.getGeo_point().getLongitude(), hasilDistance);

//                            result[i][0] = selectedValueList.get(i);
//                            result[i][1] = hasil[0];
                            Log.d(TAG, "ke : " + selectedValueList.get(i) + " distance " + hasilDistance[0]);
                            current[i] = (float) (hasilDistance[0] * 0.001);
                            node_current[i] = current[i];
                            Log.d(TAG, "hasill : " + Arrays.toString(current));
//                            current = hasil[0];

                            break;
//
                        }
                    }
                }
                Log.d(TAG, "getCurrent: " + Arrays.toString(node_current));
                getUrutan();

            }
        });

    }


//        private interface FirestoreCallback {
//            void onCallback(double[] node_current);
//        }


//    private long calculateDirections(GeoPoint tujuan) {
//
//        Log.d(TAG, "calculateDirections: calculating directions.");
//
//        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
//                tujuan.getLatitude(),
//                tujuan.getLongitude()
//        );
//        Log.d(TAG, "calculateDirections: tujuan lat : " + tujuan.getLatitude());
//        Log.d(TAG, "calculateDirections: tujuan long : " + tujuan.getLongitude());
//        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);
//
//        directions.alternatives(true);
//        directions.origin(
//                new com.google.maps.model.LatLng(
//                        mLokasiUser.getGeo_point().getLatitude(),
//                        mLokasiUser.getGeo_point().getLongitude()
//                )
//        );
//        Log.d(TAG, "calculateDirections: user current :" + mLokasiUser.getGeo_point().toString());
//        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
//        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
//            @Override
//            public void onResult(DirectionsResult result) {
//                Log.d(TAG, "onResult: success");
//                Log.d(TAG, "onResult: success calculate: distance: " + result.routes[0].legs[0].distance.inMeters);
//
//            }
//
////                distance[0] = result.routes[0].legs[0].distance.inMeters;
////                Log.d(TAG, "onResult: success calculate: distance variable: " + distance[0]);
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());
//
//            }
//        });
//        return distance;
//
//    }


}
