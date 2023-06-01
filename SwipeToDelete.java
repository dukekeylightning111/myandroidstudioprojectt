package com.example.myapplication;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDelete extends ItemTouchHelper.Callback {

    private SportingActivityAdapter adapter;
    private AppDatabase appDatabase;
    private Context context;

    public SwipeToDelete(SportingActivityAdapter adapter, AppDatabase appDatabase, Context context) {
        this.adapter = adapter;
        this.appDatabase = appDatabase;
        this.context = context;
    }

    public SwipeToDelete(SportingActivityAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        showConfirmationDialog(position);
    }

    private void showConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(  "האם אתם בטוחים שאתם רוצים למחוק פעולה זו? " )
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SportingActivityClass sportingActivity = adapter.getSportingActivityAtPosition(position);
                        new Thread(() -> {
                            appDatabase.SportingActivityClassDao().delete(sportingActivity);
                            // Notify adapter that item has been removed
                            adapter.notifyItemRemoved(position);
                        }).start();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Notify adapter to update view
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();
    }
}