package com.codebee.waterrider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.waterrider.dto.AuthDTO;
import com.codebee.waterrider.dto.CustomerChatDto;
import com.codebee.waterrider.dto.OrderDto;
import com.codebee.waterrider.dto.OrderItemDto;
import com.codebee.waterrider.service.RequestService;
import com.codebee.waterrider.service.WaterAppService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    ArrayAdapter<String> cityArrayAdapter;
    List<String> cities = new ArrayList<String>();

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);
        WaterAppService requestService = RequestService.getRequestService();
        Call<List<CustomerChatDto>> allCustomers = requestService.getAllCustomers(authDTO, token);
        allCustomers.enqueue(new Callback<List<CustomerChatDto>>() {
            @Override
            public void onResponse(Call<List<CustomerChatDto>> call, Response<List<CustomerChatDto>> response) {

                if (response.isSuccessful()) {
                    List<CustomerChatDto> body = response.body();
                    body.forEach(client -> {
                        System.out.println(client.getFirstName());
                        cities.add(client.getFirstName() + " " + client.getLastName() + " " + client.getMobile() + " " + client.getEmail());
                    });

                    Spinner spinner = fragment.findViewById(R.id.spinnerClient);
                    cityArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                    cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(cityArrayAdapter);
                    spinner.setSelection(0);

                }
            }

            @Override
            public void onFailure(Call<List<CustomerChatDto>> call, Throwable t) {
                System.out.println(t);
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] lastid = new String[1];

//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

        Spinner spin = fragment.findViewById(R.id.spinnerClient);

        fragment.findViewById(R.id.sendImage).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText text = fragment.findViewById(R.id.messageTxt);


                if (text.getText().toString().isEmpty()) {

                    Toast.makeText(getActivity(), "text is Empty", Toast.LENGTH_SHORT).show();

                } else {

                    String s = spin.getSelectedItem().toString();
                    SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

                    double num = Double.parseDouble(lastid[0]);
                    double v1 = num + 0.1;
                    Map<String, Object> message = new HashMap<>();
                    System.out.println(v1);
                    message.put("id", String.valueOf(v1));
                    message.put("date", new Date());
                    message.put("customer_mobile", s.split(" ")[3]);
                    message.put("driver_mobile", email);
                    message.put("status", "2");
                    message.put("message", text.getText().toString());


                    String token = prefs.getString("accessToken", "no");

                    db.collection("messages")
                            .add(message)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    text.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });

                }
            }
        });


        fragment.findViewById(R.id.callintent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + spin.getSelectedItem().toString().split(" ")[2]));
                startActivity(intent);
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = spin.getSelectedItem().toString();
                System.out.println(selectedItem);
                List<String> cities = new ArrayList<>();
                List<String> ids = new ArrayList<>();
                RecyclerView recyclerView = fragment.findViewById(R.id.chatRecycler);
                cities.clear();
                ids.clear();

                db.collection("messages")
                        .whereEqualTo("customer_mobile", selectedItem.split(" ")[3])
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("TAG", "Listen failed.", e);
                                    return;
                                }

                                cities.clear();
                                ids.clear();
                                ArrayList<String> integers = new ArrayList<>();

                                for (QueryDocumentSnapshot doc : value) {
                                    integers.add((String) doc.get("id"));
//                                                                    if (doc.get("message") != null) {
//                                                                        cities.add(doc.getString("message"));
//                                                                    }
//                                                                    if (doc.get("status") != null) {
//                                                                        ids.add(doc.getString("status"));
//                                                                    }
                                }


                                Collections.sort(integers);
                                for (int i = 0; i < integers.size(); i++) {
                                    for (QueryDocumentSnapshot doc : value) {

                                        String integer = integers.get(i);
                                        String id = (String) doc.get("id");

                                        if (id.equals(integer)) {
                                            if (doc.get("message") != null) {
                                                cities.add(i, doc.getString("message"));
                                                lastid[0] = id;
                                            }
                                            if (doc.get("status") != null) {
                                                ids.add(i, doc.getString("status"));
                                            }

                                        }
//
                                    }


                                }

                                Log.d("TAG", "Current cites in CA: " + cities);

                                if (cities.size() != 0) {
                                    RecyclerView.Adapter<ChatVH> adapter = new RecyclerView.Adapter<ChatVH>() {
                                        @NonNull
                                        @Override
                                        public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                            View inflate = inflater.inflate(R.layout.chat_layout_small, parent, false);

                                            return new ChatVH(inflate);
                                        }

                                        @Override
                                        public void onBindViewHolder(@NonNull ChatVH holder, int position) {

                                            if ((ids.get(position)).equals("1")) {
                                                TextView textView = holder.getTextView();
                                                textView.setText(cities.get(position).toString());
                                            } else {
                                                TextView textView = holder.getTextView2();
                                                textView.setText(cities.get(position).toString());

                                            }


                                        }


                                        @Override
                                        public int getItemCount() {
                                            return cities.size();
                                        }
                                    };


                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerView.setAdapter(adapter);
                                } else {

                                }

                            }
                        });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}

class ChatVH extends RecyclerView.ViewHolder {
    public TextView getTextView2() {
        return textView2;
    }

    private TextView textView, textView2;

    public ChatVH(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.chatTextApp);
        textView2 = itemView.findViewById(R.id.chatTextApp2);

    }


    public TextView getTextView() {
        return textView;
    }
}

