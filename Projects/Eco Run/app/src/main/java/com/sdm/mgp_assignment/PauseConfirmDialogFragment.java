package com.sdm.mgp_assignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PauseConfirmDialogFragment extends DialogFragment
{
    public static boolean IsShown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        IsShown = true;

        //Use the Builder class for creating the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Change the message based on the pause state
        String message = GameSystem.Instance.GetIsPaused() ? "Confirm Unpause?" : "Confirm Pause?";

        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        GameSystem.Instance.SetIsPaused(!GameSystem.Instance.GetIsPaused());
                        IsShown = false;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // We will define what to do when -ve button is pressed
                        // Do not want to pause/unpause.
                        IsShown = false;
                    }
                });
        return builder.create();
    }

    @Override
    public void onCancel (DialogInterface dialog)
    {
        IsShown = false;
    }

    @Override
    public void onDismiss (DialogInterface dialog)
    {
        IsShown = false;
    }
}