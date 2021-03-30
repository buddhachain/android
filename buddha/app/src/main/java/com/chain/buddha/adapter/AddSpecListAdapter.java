package com.chain.buddha.adapter;

import android.content.Context;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.StringUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class AddSpecListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private List<EditText[]> editTextList = new ArrayList<>();

    public AddSpecListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_add_spec, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        String[] list = strings.split(",");
        if (editTextList.size() <= baseViewHolder.getLayoutPosition()) {
            editTextList.add(new EditText[2]);
        }
        EditText mDescEt = baseViewHolder.getView(R.id.et_spec_desc);
        editTextList.get(baseViewHolder.getLayoutPosition())[0] = mDescEt;
        mDescEt.setText(list[2]);

        EditText mPriceEt = baseViewHolder.getView(R.id.et_spec_price);
        editTextList.get(baseViewHolder.getLayoutPosition())[1] = mPriceEt;
        mPriceEt.setText(list[3]);

    }

    /**
     * 检查输入
     *
     * @return
     */
    public boolean checkInput() {
        boolean hasNull = false;
        for (EditText[] editTexts : editTextList) {
            if (StringUtils.equalsHasNull(editTexts[0].getText().toString(), editTexts[1].getText().toString())) {
                hasNull = true;
                break;
            }
        }
        return !hasNull;

    }

    public String getSpecString() {
        List<HashMap<String, String>> specList = new ArrayList<>();
        for (int i = 1; i <= editTextList.size(); i++) {
            EditText[] editTexts = editTextList.get(i - 1);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("sequence", i + "");
            hashMap.put("desc", editTexts[0].getText().toString());
            hashMap.put("price", editTexts[1].getText().toString());
            specList.add(hashMap);
        }
        return new Gson().toJson(specList);
    }

}
