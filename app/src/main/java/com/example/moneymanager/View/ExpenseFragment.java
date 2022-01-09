package com.example.moneymanager.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Edit_New_Cost;
import com.example.moneymanager.MainActivity;
import com.example.moneymanager.Model.Expense;
import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.R;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {
    static final String tb_name = "MoneyTable";
    private ArrayList<Expense> expenseList;
    private Expense_Adapter expense_adapter;
    private SQLiteDatabase db;

    private View view;
    private Cursor cur;
    private TextView no_data_txv;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expense_fragment, container, false);
        no_data_txv = (TextView) view.findViewById(R.id.no_data_txv);
        recyclerView = (RecyclerView) view.findViewById(R.id.expense_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // TODO 監控Fragment的位置，下拉RecycclerView隱藏Fab
        expenseList = new ArrayList<>();

        //存取DB的資料
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cur = db.rawQuery("SELECT * FROM "+tb_name,null);
        if (cur.getCount() != 0) {
            no_data_txv.setVisibility(View.GONE);
            while (cur.moveToNext()) {
                //第0筆資料是索引值
                Expense expensemodel = new Expense(cur.getString(1),cur.getString(2),cur.getString(3),cur.getString(4));
                expenseList.add(expensemodel);
            }
        }else{
            no_data_txv.setVisibility(View.VISIBLE);
        }

//        Log.wtf("size_of_my_list", Integer.toString(expenseList.size()));
        expense_adapter = new Expense_Adapter(expenseList);
        recyclerView.setAdapter(expense_adapter);
        return view;
    }

    public class Expense_Adapter extends RecyclerView.Adapter<Expense_Adapter.ViewHolder> {
        private Context context;
        private ArrayList<Expense> expenseList; //拆成多個ArrayList創建Recyclerview

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView expense_date_txv, expense_num_txv, expense_memo_txv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                expense_date_txv = itemView.findViewById(R.id.show_date_txv);
                expense_num_txv = itemView.findViewById(R.id.show_num_txv);
                expense_memo_txv = itemView.findViewById(R.id.show_memo_txv);

                //點擊item時可以更新資料
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cur.moveToPosition(getAdapterPosition());
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
                    }
                });
                //長按item時可以刪除資料
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //取出DB資料顯示
                        cur.moveToPosition(getAdapterPosition());
                        int id = cur.getInt(0);
                        String type = cur.getString(1);
                        String date = cur.getString(2);
                        String num = cur.getString(3);
                        String memo = cur.getString(4);

                        //刪除提示訊息
                        AlertDialog.Builder delete_dialog = new AlertDialog.Builder(getActivity());
                        delete_dialog.setTitle("刪除確認");
                        String alert_message =
                                "你確定要刪除以下這筆資料嗎?\n"+
                                "種類: "+type+"\n"+
                                "日期: "+date+"\n"+
                                "金額: "+num+"\n"+
                                "備註: "+memo;
                        delete_dialog.setMessage(alert_message);
                        delete_dialog.setCancelable(true);
                        delete_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //執行資料刪除的動作
                                MyDBHelper db = new MyDBHelper(getContext());
                                String status = db.deleteExpense(id);
                                if(status == "success"){
                                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                                    startActivity(refresh);
                                }
                            }
                        });
                        delete_dialog.show();
                        return true;
                    }
                });
            }
        }

        public Expense_Adapter(ArrayList<Expense> expenseList) {
            this.expenseList = expenseList;
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
            holder.expense_date_txv.setText(expenseList.get(position).getDate());
            holder.expense_num_txv.setText(expenseList.get(position).getNum());
            holder.expense_memo_txv.setText(expenseList.get(position).getMemo());

            //TODO 收入與支出的資料分別以不同顏色顯示於畫面上
            //TODO 每一個tab頁面最上方可以顯示下方所有資料的收入與支出合計總金額
        }

        @Override
        public int getItemCount() {
            return expenseList.size();
        }
    }
}

