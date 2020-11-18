package com.refCustomerFamily.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity;
import com.refCustomerFamily.databinding.ChatBillLeftRowBinding;
import com.refCustomerFamily.databinding.ChatImageLeftRowBinding;
import com.refCustomerFamily.databinding.ChatImageRightRowBinding;
import com.refCustomerFamily.databinding.ChatMessageLeftRowBinding;
import com.refCustomerFamily.databinding.ChatMessageRightRowBinding;
import com.refCustomerFamily.databinding.LoadMoreBinding;
import com.refCustomerFamily.models.MessageModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_image_LEFT = 3;
    private final int ITEM_image_RIGHT = 4;
    private final int ITEM_LOADMORE = 5;
    private final int itembill = 6;
    private final String lang;


    private List<MessageModel> messageModelList;
    private int current_user_id;
    private Context context;
    private LayoutInflater inflater;
    private ChatActivity activity;

    public Chat_Adapter(List<MessageModel> messageModelList, int current_user_id, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity = (ChatActivity) context;
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_RIGHT) {
            ChatMessageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row, parent, false);
            return new RightMessageEventHolder(binding);

        } else if (viewType == ITEM_MESSAGE_LEFT) {
            ChatMessageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row, parent, false);
            return new LeftMessageEventHolder(binding);

        } else if (viewType == ITEM_image_LEFT) {
            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row, parent, false);
            return new LeftImageEventHolder(binding);

        } else if (viewType == ITEM_image_RIGHT) {
            ChatImageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row, parent, false);
            return new RightImageEventHolder(binding);

        }
        else if (viewType == itembill) {
            ChatBillLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_bill_left_row, parent, false);
            return new BillEventHolder(binding);

        }
        else {

            LoadMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more, parent, false);
            return new LoadMoreHolder(binding);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        if (holder instanceof RightImageEventHolder) {
            RightImageEventHolder eventHolder = (RightImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        } else if (holder instanceof LeftImageEventHolder) {
            LeftImageEventHolder eventHolder = (LeftImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        } else if (holder instanceof RightMessageEventHolder) {
            RightMessageEventHolder eventHolder = (RightMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        } else if (holder instanceof LeftMessageEventHolder) {
            LeftMessageEventHolder eventHolder = (LeftMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }

        else if (holder instanceof BillEventHolder) {
            BillEventHolder eventHolder = (BillEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }
        else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder typingHolder = (LoadMoreHolder) holder;


        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreBinding binding;

        public LoadMoreHolder(LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public class RightMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageRightRowBinding binding;

        public RightMessageEventHolder(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageLeftRowBinding binding;

        public LeftMessageEventHolder(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageLeftRowBinding binding;

        public LeftImageEventHolder(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class RightImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageRightRowBinding binding;

        public RightImageEventHolder(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class BillEventHolder extends RecyclerView.ViewHolder {
        public ChatBillLeftRowBinding binding;

        public BillEventHolder(@NonNull ChatBillLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelList.get(position);
        //  Log.e("lsllsl", current_user_id + " " + messageModel.getTo_user_id());
        if (messageModel == null) {

            return ITEM_LOADMORE;

        } else if (messageModel.getTo_user_id().equals(current_user_id + "")) {
            //  Log.e("type",messageModel.getType());
            if (messageModel.getType().equals("text")) {
                return ITEM_MESSAGE_LEFT;
            }
            else  if (messageModel.getType().equals("text_file")) {
                return itembill;
            }

            else {
                return ITEM_image_LEFT;
            }
        } else {
            if (messageModel.getType().equals("text")) {
                return ITEM_MESSAGE_RIGHT;
            }

            else {
                return ITEM_image_RIGHT;
            }


        }
    }

}

