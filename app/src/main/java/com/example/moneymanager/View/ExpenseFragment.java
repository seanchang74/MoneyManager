package com.example.moneymanager.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Edit_New_Cost;
import com.example.moneymanager.MainActivity;
import com.example.moneymanager.Model.Expense;
import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseFragment extends Fragment implements TextWatcher {
    static final String tb_name = "MoneyTable";
    private ArrayList<Expense> expenseList;
    private Expense_Adapter expense_adapter;
    private SQLiteDatabase db;

    private Integer initID;
    private View view;
    private Cursor cur;
    private Calendar mcal;
    private TextView no_data_txv, selectdate;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expense_fragment, container, false);
        String timeStamp = new SimpleDateFormat("yyyy/M/d").format(Calendar.getInstance().getTime());
        no_data_txv = (TextView) view.findViewById(R.id.no_data_txv);
        recyclerView = (RecyclerView) view.findViewById(R.id.expense_recycler);
        expenseList = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        selectdate = view.findViewById(R.id.select_date);
        selectdate.setText(timeStamp);
        //日期字串的監聽函式
        selectdate.addTextChangedListener(this);
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //觸發DatePickerDialog
                mcal = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mcal.set(year,monthOfYear,dayOfMonth);
                        selectdate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                    }
                },
                        mcal.get(Calendar.YEAR),
                        mcal.get(Calendar.MONTH),
                        mcal.get(Calendar.DATE)).show();
            }
        });

        //存取DB的資料
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cur = db.rawQuery("SELECT * FROM "+tb_name,null);
        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                //篩選符合日期的資料
                if(cur.getString(2).equals(selectdate.getText().toString())){
                    //第0筆資料是索引值
                    Expense expensemodel = new Expense(cur.getInt(0),cur.getString(1),cur.getString(2),cur.getString(3),cur.getString(4));
                    expenseList.add(expensemodel);
                }
            }
        }

        if(expenseList.size()!=0){
            no_data_txv.setVisibility(View.GONE);
        }else{
            no_data_txv.setVisibility(View.VISIBLE);
        }

        expense_adapter = new Expense_Adapter(expenseList,initID);
        recyclerView.setAdapter(expense_adapter);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    //日期改變後重新取資料
    @Override
    public void afterTextChanged(Editable editable) {
        //動態更新顯示資料
        expenseList.clear();
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cur = db.rawQuery("SELECT * FROM "+tb_name,null);
        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                //篩選符合日期的資料
                if(cur.getString(2).equals(selectdate.getText().toString())){
                    //第0筆資料是索引值
                    Expense expensemodel = new Expense(cur.getInt(0),cur.getString(1),cur.getString(2),cur.getString(3),cur.getString(4));
                    expenseList.add(expensemodel);
                }
            }
        }

        if(expenseList.size()!=0){
            no_data_txv.setVisibility(View.GONE);
        }else{
            no_data_txv.setVisibility(View.VISIBLE);
        }

        //更新RecyclerView畫面
        expense_adapter.notifyDataSetChanged();
    }

    public class Expense_Adapter extends RecyclerView.Adapter<Expense_Adapter.ViewHolder> {
        private Integer initID;
        private ArrayList<Expense> expenseList; //拆成多個ArrayList創建Recyclerview

        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView data_card;
            TextView expense_type_txv, expense_num_txv, expense_memo_txv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                data_card = itemView.findViewById(R.id.Data_card);
                expense_type_txv = itemView.findViewById(R.id.show_type_txv);
                expense_num_txv = itemView.findViewById(R.id.show_num_txv);
                expense_memo_txv = itemView.findViewById(R.id.show_memo_txv);

                //點擊item時可以更新資料
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Expense expense = getExpenseList().get(getAdapterPosition());
                        Integer expense_id = expense.getId();
                        cur = db.rawQuery("SELECT * FROM MoneyTable WHERE _id = '"+expense_id+"'", null);
                        if(cur.moveToFirst()){
                            String type = cur.getString(1);
                            String date = cur.getString(2);
                            String num = cur.getString(3);
                            String memo = cur.getString(4);

                            Intent intent = new Intent(getActivity(), Edit_New_Cost.class);
                            intent.putExtra("id", cur.getInt(0));
                            intent.putExtra("type", type);
                            intent.putExtra("date", date);
                            intent.putExtra("num", num);
                            intent.putExtra("memo", memo);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "發生錯誤", Toast.LENGTH_SHORT).show();
                            Log.wtf("No data","No matching data");
                        }
                    }
                });
                //長按item時可以刪除資料
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Expense expense = getExpenseList().get(getAdapterPosition());
                        Integer expense_id = expense.getId();
                        cur = db.rawQuery("SELECT * FROM MoneyTable WHERE _id = '"+expense_id+"'", null);

                        if(cur.moveToFirst()) {
                            int id = cur.getInt(0);
                            String type = cur.getString(1);
                            String date = cur.getString(2);
                            String num = cur.getString(3);
                            String memo = cur.getString(4);

                            //刪除提示訊息
                            AlertDialog.Builder delete_dialog = new AlertDialog.Builder(getActivity());
                            delete_dialog.setTitle("刪除確認");
                            String alert_message =
                                    "你確定要刪除以下這筆資料嗎?\n" +
                                            "種類: " + type + "\n" +
                                            "日期: " + date + "\n" +
                                            "金額: " + num + "\n" +
                                            "備註: " + memo;
                            delete_dialog.setMessage(alert_message);
                            delete_dialog.setCancelable(true);
                            delete_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    expenseList.remove(getAdapterPosition());
                                    expense_adapter.notifyItemRemoved(getAdapterPosition());
                                    //執行資料刪除的動作
                                    MyDBHelper db = new MyDBHelper(getContext());
                                    String status = db.deleteExpense(id);
                                    if (status == "success") {
                                        Intent refresh = new Intent(getActivity(), MainActivity.class);
                                        startActivity(refresh);
                                    }
                                }
                            });
                            delete_dialog.show();
                        }else{
                            Toast.makeText(getContext(), "發生錯誤", Toast.LENGTH_SHORT).show();
                            Log.wtf("No data","No matching data");
                        }
                        return true;
                    }
                });
            }
        }

        public Expense_Adapter(ArrayList<Expense> expenseList, Integer initID) {
            this.initID = initID;
            this.expenseList = expenseList;
        }

        public ArrayList<Expense> getExpenseList() {
            return expenseList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cardview_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String type[] = new String[] {"飲食", "交通", "娛樂", "醫療", "社交"};
            holder.data_card.setCardBackgroundColor(Color.parseColor("#FFCCBC"));
            //收入與支出以不同顏色區分
            for(int i=0;i<type.length;i++){
                if(expenseList.get(position).getType().equals(type[i])){
                    holder.data_card.setCardBackgroundColor(Color.parseColor("#88b1f2"));
                    break;
                }
            }
            holder.expense_type_txv.setText(expenseList.get(position).getType());
            holder.expense_num_txv.setText(expenseList.get(position).getNum());
            holder.expense_memo_txv.setText(expenseList.get(position).getMemo());
        }

        @Override
        public int getItemCount() {
            return expenseList.size();
        }
    }
}

