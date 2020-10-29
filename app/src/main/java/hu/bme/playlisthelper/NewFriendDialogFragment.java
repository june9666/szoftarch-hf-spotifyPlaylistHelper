package hu.bme.playlisthelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class NewFriendDialogFragment extends DialogFragment {

    public static final String TAG = "NewFriendDialogFragment";

    public interface NewFriendDialogListener {
        void onFriendItemCreated(FriendItem newItem);
    }

    private NewFriendDialogListener listener;

    private EditText nameEditText;
    private EditText usernameEditText;
    private Spinner categorySpinner;
    private CheckBox isDefaultCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewFriendDialogListener) {
            listener = (NewFriendDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewFriendDialogListener interface!");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        return alertDialogBuilder.create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_item, null);
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText);
        descriptionEditText = contentView.findViewById(R.id.ShoppingItemDescriptionEditText);
        estimatedPriceEditText = contentView.findViewById(R.id.ShoppingItemEstimatedPriceEditText);
        categorySpinner = contentView.findViewById(R.id.ShoppingItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox);
        return contentView;
    }

}
