package com.fatech.mapwalker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinishDialog extends DialogFragment {
    private HashMap<Integer, String> products = new HashMap<>();

//    private void setProductList() {
//        products.put(167, "Куриное мясо");
//        products.put(203, "Баранина");
//        products.put(157, "Куриное яйцо");
//        products.put(250, "Икра красная");
//        products.put(45, "Яблоки");
//        products.put(90, "Банан");
//        products.put(38, "Клубника");
//        products.put(58, "Киви");
//        products.put(40, "Арбуз");
//        products.put(33, "Морковь");
//        products.put(15, "Огурец");
//        products.put(16, "Редис");
//        products.put(368, "Гречка");
//        products.put(374, "Овсянная каша");
//        products.put(264, "Батон хлеба");
//        products.put(56, "Борщ");
//        products.put(43, "Овощной суп");
//        products.put(30, "Грибной суп");
//        products.put(40, "Щи");
//        products.put(243, "Яичница");
//        products.put(160, "Варенные яйца");
//        products.put(129, "Овощное рагу");
//        products.put(563, "Шпроты");
//        products.put(259, "Рыбные котлеты");
//        products.put(142, "Ролл Филадельфия");
//        products.put(176, "Калифорния");
//        products.put(76, "Винегрет");
//        products.put(188, "Греческий салат");
//        products.put(301, "Цезарь");
//        products.put(324, "Шашлык куриный");
//        products.put(180, "Шашлык говяжий");
//        products.put(257, "Колбаса вареная докторская");
//        products.put(304, "Мясо по-французки");
//        products.put(321, "Бутерброд с сыром");
//        products.put(510, "Чипсы");
//        products.put(438, "БигМак");
//    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            products.put(167, "Куриное мясо");
            products.put(203, "Баранина");
            products.put(157, "Куриное яйцо");
            products.put(250, "Икра красная");
            products.put(45, "Яблоки");
            products.put(90, "Банан");
            products.put(38, "Клубника");
            int KKal = (int) getArguments().get("KKal");
            for (Map.Entry product : products.entrySet()) {
                if (KKal < (int) product.getKey()) {
                    products.remove(product.getKey());
                }
            }
            List<String> listProducts = new ArrayList<>();
            for (Map.Entry product : products.entrySet()) {
                listProducts.add(product.getValue() + " (" + product.getKey() + " KKal)");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "Вы дошли до конца и прошли все километры. Вы сожгли " + KKal + " ККал. Столько же калорий содержится в:";
            return builder
                    .setIcon(R.drawable.man_finish_dialog)
                    .setTitle("Поздравляем!")
                    .setMessage(message)
                    .setView(R.layout.finish_dialog).create();
        } catch (Exception e) {
            return null;
        }
    }

}
