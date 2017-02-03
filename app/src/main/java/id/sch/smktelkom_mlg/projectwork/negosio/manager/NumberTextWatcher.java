package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by LittleFireflies on 04-Feb-17.
 */

public class NumberTextWatcher implements TextWatcher{

    private  ProgressBar wait;
    private  DecimalFormat df;
    private  DecimalFormat dfnd;
    private final EditText et;
    String formatInput;
    private boolean hasFractionalPart;
    private int trailingZeroCount;

    public NumberTextWatcher(EditText editText, String pattern, String format, ProgressBar codePromoProgress) {
        this.formatInput=format;
        if(formatInput.equals("currency"))
        {
            df = new DecimalFormat(pattern);
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,###");
            this.et = editText;
            hasFractionalPart = false;
        }
        else {
            wait=codePromoProgress;
            wait.setVisibility(View.GONE);
            this.et = editText;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(formatInput.equals("currency"))
        {
            int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
            trailingZeroCount = 0;
            if (index > -1) {
                for (index++; index < s.length(); index++) {
                    if (s.charAt(index) == '0')
                        trailingZeroCount++;
                    else {
                        trailingZeroCount = 0;
                    }
                }
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);
        if(formatInput.equals("currency")){
            if (s != null && !s.toString().isEmpty()) {
                try {
                    int inilen, endlen;
                    inilen = et.getText().length();
                    String v = s.toString().replace(".", "");
                    Number n = null;
                    try {
                        n = df.parse(v);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int cp = et.getSelectionStart();
//                if (hasFractionalPart) {
//                    StringBuilder trailingZeros = new StringBuilder();
//                    while (trailingZeroCount-- > 0)
//                        trailingZeros.append('0');
//                    String z=df.format(n);
//                    z=z.replace(",",".");
//                    et.setText(z);
//                } else {
                    String z=dfnd.format(n);
                    z=z.replace(",",".");
                    et.setText(z);
//                }
//                et.setText("$".concat(et.getText().toString()));
                    endlen = et.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel < et.getText().length()) {
                        et.setSelection(sel);
                    } else if (trailingZeroCount > -1) {
                        et.setSelection(et.getText().length());
                    } else {
                        et.setSelection(et.getText().length());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        else {
//            getDataPomo(et.getText().toString());
        }
        et.addTextChangedListener(this);
    }
}
