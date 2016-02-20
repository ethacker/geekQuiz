package com.example.group21.homework3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ethan on 2/19/16.
 */
public class Questions implements Parcelable {
    int questionID;
    String questionText;
    String [] questionOptions;
    int [] optionValues;
    String imageURL;
    int questionLength;
    int numOptions;
    boolean hasImage;


    public Questions(int questionID, String questionText, String[] questionOptions, int[] optionValues, String imageURL) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.questionOptions = questionOptions;
        this.optionValues = optionValues;
        this.imageURL = imageURL;
    }

    public Questions(String s){

        String [] questionParts = s.split(";");

        questionLength = questionParts.length;
        questionID = Integer.valueOf(questionParts[0]);
        questionText = questionParts[1];

        if(questionParts[questionLength-1].startsWith("http")){
            hasImage = true;
        }

        if(hasImage) {
            questionOptions = new String[questionLength-3];

            for (int i = 2; i < (questionLength - 1); i++) {

                questionOptions[i-2] = questionParts[i];

            }
            numOptions = questionOptions.length;
            imageURL = questionParts[questionLength-1];
        }else{

            questionOptions = new String[questionLength-2];

            for(int i=2;i<questionLength;i++){

                questionOptions[i=2] = questionParts[i];
            }
            numOptions = questionOptions.length;
            imageURL = null;

        }



    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(questionID);
        dest.writeByte((byte) (hasImage ? 1 : 0));
        dest.writeString(questionText);
        dest.writeInt(numOptions);
        dest.writeString(imageURL);
        dest.writeStringArray(questionOptions);





    }

    public Questions(Parcel in){

        this.questionID = in.readInt();
        this.hasImage = in.readByte() !=0;
        this.questionText = in.readString();
        this.numOptions = in.readInt();
        this.imageURL = in.readString();
        this.questionOptions = in.createStringArray();



    }


    public static final Parcelable.Creator<Questions> CREATOR
            = new Parcelable.Creator<Questions>() {
        public Questions createFromParcel(Parcel in) {return new Questions(in);
        }

        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };
}
