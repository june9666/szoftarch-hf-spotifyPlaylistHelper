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

    private EditText nameEditText;
    private EditText usernameEditText;
    private Spinner categorySpinner;
    private CheckBox isDefaultCheckBox;
    private NewFriendDialogListener listener;

    public interface NewFriendDialogListener {
        void onFriendItemCreated(FriendItem newItem);
    }

    public NewFriendDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NewFriendDialogFragment newInstance() {
        NewFriendDialogFragment frag = new NewFriendDialogFragment();

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewFriendDialogListener) {
            listener = (NewFriendDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_friend)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onFriendItemCreated(getFriendItem());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_friend, null);
        nameEditText = contentView.findViewById(R.id.AddFriendFriendNameEditText);
        usernameEditText = contentView.findViewById(R.id.AddFriendSpotifyUsernameEditText);

        categorySpinner = contentView.findViewById(R.id.AddFriendCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        isDefaultCheckBox = contentView.findViewById(R.id.AddFriendIsDefaultCheckBox);
        return contentView;
    }
    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private FriendItem getFriendItem() {
        FriendItem friendItem = new FriendItem();
        friendItem.name = nameEditText.getText().toString();
        friendItem.username = usernameEditText.getText().toString();

        friendItem.category = FriendItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        friendItem.isDefault = isDefaultCheckBox.isChecked();
        return friendItem;
    }


}
