package com.santtytech.directfirst;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        User user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getUsername());

        RecyclerView rv = findViewById(R.id.rcl_send);

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
    }

    public class MessageItem extends Item<ViewHolder> {

        private final boolean isLeft;

        public MessageItem(boolean isLeft) {
            this.isLeft = isLeft;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return isLeft ? R.layout.item_from_message : R.layout.item_to_message;
        }
    }
}
