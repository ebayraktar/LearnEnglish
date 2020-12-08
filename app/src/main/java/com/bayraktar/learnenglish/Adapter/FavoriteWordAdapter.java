package com.bayraktar.learnenglish.Adapter;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class FavoriteWordAdapter extends RecyclerView.Adapter<FavoriteWordAdapter.ViewHolder> {
    List<Word> wordList;
    IFavoriteWordListener favoriteWordListener;
    int currentLang;
    TextToSpeech myTTS;


    public FavoriteWordAdapter(int currentLang, TextToSpeech myTTS, IFavoriteWordListener favoriteWordListener) {
        this.currentLang = currentLang;
        wordList = new ArrayList<>();
        this.myTTS = myTTS;
        this.favoriteWordListener = favoriteWordListener;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    public void setCurrentLang(int currentLang) {
        this.currentLang = currentLang;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_word, parent, false);
        return new ViewHolder(view, favoriteWordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Word word = wordList.get(position);

        holder.folding_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.folding_cell.toggle(false);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String json = (String) word.getAdditionalProperties().getOrDefault("userWordInformation", "");
            UserWordInformation userWordInformation = new Gson().fromJson(json, UserWordInformation.class);
            assert userWordInformation != null;
            if (userWordInformation.isFav()) {
                holder.ivAddFav.setImageResource(R.drawable.ic_favorite_24);
            } else {
                holder.ivAddFav.setImageResource(R.drawable.ic_un_favorite_24);
            }
            switch (userWordInformation.getApproved()) {
                case 1:
                    holder.ivWordIsApproved.setImageResource(R.drawable.ic_approved);
                    break;
                case 2:
                    holder.ivWordIsApproved.setImageResource(R.drawable.ic_unapproved);
                    break;
                default:
                    holder.ivWordIsApproved.setImageResource(R.drawable.ic_none_approved_24);
                    break;
            }
        }
        holder.tvWordText.setText(word.getLanguage().get(currentLang).getCode());
        holder.ivWordSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                myTTS.speak(word.getLanguage().get(currentLang).getCode(), TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });
        holder.tvTitleWord.setText(word.getLanguage().get(currentLang).getCode());

        final List<Integer> Indexes = new ArrayList<>();
        Indexes.add(0);
        Indexes.add(0);
        holder.tvDefinitions.setText(word.getLanguage().get(currentLang).getDefinitions().get(Indexes.get(0)));
        holder.tvExamples.setText(word.getLanguage().get(currentLang).getExamples().get(Indexes.get(1)));
        Glide.with(holder.itemView)
                .load(word.getImages().get(0))
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_terrain_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(holder.ivWordImage);
        holder.ivNextDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Indexes.get(0);
                index++;
                if (index >= word.getLanguage().get(currentLang).getDefinitions().size()) {
                    index = 0;
                }
                Indexes.set(0, index);
                holder.tvDefinitions.setText(word.getLanguage().get(currentLang).getDefinitions().get(Indexes.get(0)));
            }
        });
        holder.ivNextExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Indexes.get(1);
                index++;
                if (index >= word.getLanguage().get(currentLang).getExamples().size()) {
                    index = 0;
                }
                Indexes.set(1, index);
                holder.tvExamples.setText(word.getLanguage().get(currentLang).getExamples().get(Indexes.get(1)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final FoldingCell folding_cell;

        //Title
        final TextView tvWordText;
        final TextView tvDispersionValue;
        final TextView tvFrequencyValue;
        final ImageView ivWordSound;

        //Content
        final TextView tvTitleWord;
        final ImageView ivWordImage;

        final ImageView ivWordIsApproved;
        final ImageView ivAddFav;
        final ImageView ivNextDefinition;
        final ImageView ivNextExample;

        final TextView tvDefinitions;
        final TextView tvExamples;

        final CardView cvDefinitions;
        final CardView cvExamples;

        IFavoriteWordListener favoriteWordListener;

        public ViewHolder(@NonNull View itemView, IFavoriteWordListener favoriteWordListener) {
            super(itemView);
            folding_cell = itemView.findViewById(R.id.folding_cell);

            //Title
            tvWordText = itemView.findViewById(R.id.tvWordText);
            tvDispersionValue = itemView.findViewById(R.id.tvDispersionValue);
            tvFrequencyValue = itemView.findViewById(R.id.tvFrequencyValue);

            ivWordSound = itemView.findViewById(R.id.ivWordSound);

            //Content
            tvTitleWord = itemView.findViewById(R.id.tvTitleWord);
            ivWordImage = itemView.findViewById(R.id.ivWordImage);

            ivWordIsApproved = itemView.findViewById(R.id.ivWordIsApproved);
            ivAddFav = itemView.findViewById(R.id.ivAddFav);
            ivNextDefinition = itemView.findViewById(R.id.ivNextDefinition);
            ivNextExample = itemView.findViewById(R.id.ivNextExample);

            tvDefinitions = itemView.findViewById(R.id.tvDefinitions);
            tvExamples = itemView.findViewById(R.id.tvExamples);

            cvDefinitions = itemView.findViewById(R.id.cvDefinitions);
            cvExamples = itemView.findViewById(R.id.cvExamples);

            ivWordIsApproved.setOnClickListener(this);
            ivAddFav.setOnClickListener(this);
            /*
            ivNextDefinition.setOnClickListener(this);
            ivNextExample.setOnClickListener(this);
             */
            cvDefinitions.setOnClickListener(this);
            cvExamples.setOnClickListener(this);

            this.favoriteWordListener = favoriteWordListener;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivWordIsApproved:
                    favoriteWordListener.onApproveClick(getAdapterPosition());
                    break;
                case R.id.ivAddFav:
                    favoriteWordListener.onFavClick(getAdapterPosition());
                    break;
                    /*
                case R.id.ivNextDefinition:
                    favoriteWordListener.onDefinitionNextClick(getAdapterPosition());
                    break;
                case R.id.ivNextExample:
                    favoriteWordListener.onExampleNextClick(getAdapterPosition());
                    break;
                    */
                case R.id.cvDefinitions:
                    favoriteWordListener.onDefinitionsClick(getAdapterPosition());
                    break;
                case R.id.cvExamples:
                    favoriteWordListener.onExamplesClick(getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface IFavoriteWordListener {
        void onFavClick(int position);

        void onApproveClick(int position);

        /*
                void onDefinitionNextClick(int position);

                void onExampleNextClick(int position);
        */
        void onDefinitionsClick(int position);

        void onExamplesClick(int position);
    }
}
