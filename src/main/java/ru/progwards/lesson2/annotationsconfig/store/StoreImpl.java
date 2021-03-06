package ru.progwards.lesson2.annotationsconfig.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import ru.progwards.lesson2.annotationsconfig.account.Account;
import ru.progwards.lesson2.annotationsconfig.account.IAccount;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component("storeImpl")
public class StoreImpl<E extends IAccount> implements Store<E> {

    private final Map<Integer, E> accounts = new ConcurrentHashMap<>();
    private final static String DB_PATH
            = "C:\\Intellij Idea\\programming\\TestSpring\\src\\main\\resources\\accounts.json";

    public StoreImpl() {
        initAccounts();
    }

    public void initAccounts() {
        if (accounts.size() == 0) {
            StoreInitializationAccounts.getAccountsList()
                    .forEach(account -> accounts.put(account.getId(), (E) account));
            accounts.values().forEach(this::write);
        }
    }

    @Override
    public void write(E item) {
        synchronized (this) {
            accounts.put(item.getId(), item);
            try (Writer writer = new FileWriter(DB_PATH)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(getAll(), writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<E> read() {
        synchronized (this) {
            try {
                accounts.clear();
                String json = Files.readString(Path.of(DB_PATH));
                Type type = new TypeToken<List<Account>>() {
                }.getType();
                ArrayList<E> list = new Gson().fromJson(json, type);
                list.forEach(e -> accounts.put(e.getId(), e));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return accounts.values().stream().collect(Collectors.toUnmodifiableList());
        }
    }

    private List<E> getAll() {
        return accounts.values().stream().collect(Collectors.toUnmodifiableList());
    }
}
