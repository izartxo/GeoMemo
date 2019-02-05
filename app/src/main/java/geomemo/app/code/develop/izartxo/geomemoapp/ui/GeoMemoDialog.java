package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.google.android.gms.maps.model.LatLng;

import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;

public class GeoMemoDialog extends DialogFragment {

    Context mContext;
    LatLng mLatLng;
    boolean mError;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.geomemo_dialog_layout, null))
                // Add action buttons
                .setPositiveButton(R.string.dialog_geomemo_accept_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("name", "xxxx");
                        intent.putExtra("input", "yyyyyy");
                        intent.putExtra("latlon", "43.04837834, -2.343443");
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    }
                })
                .setNegativeButton(R.string.dialog_geomemo_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }


}
