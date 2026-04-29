package com.kiit.foodfinder.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PreferencesDao_Impl implements PreferencesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PreferencesEntity> __insertionAdapterOfPreferencesEntity;

  public PreferencesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPreferencesEntity = new EntityInsertionAdapter<PreferencesEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `preferences` (`id`,`last_hostel_id`,`recent_searches`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PreferencesEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getLastHostelId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getLastHostelId());
        }
        statement.bindString(3, entity.getRecentSearchesRaw());
      }
    };
  }

  @Override
  public Object upsert(final PreferencesEntity prefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPreferencesEntity.insert(prefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PreferencesEntity> observe() {
    final String _sql = "SELECT * FROM preferences WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"preferences"}, new Callable<PreferencesEntity>() {
      @Override
      @Nullable
      public PreferencesEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLastHostelId = CursorUtil.getColumnIndexOrThrow(_cursor, "last_hostel_id");
          final int _cursorIndexOfRecentSearchesRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "recent_searches");
          final PreferencesEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpLastHostelId;
            if (_cursor.isNull(_cursorIndexOfLastHostelId)) {
              _tmpLastHostelId = null;
            } else {
              _tmpLastHostelId = _cursor.getString(_cursorIndexOfLastHostelId);
            }
            final String _tmpRecentSearchesRaw;
            _tmpRecentSearchesRaw = _cursor.getString(_cursorIndexOfRecentSearchesRaw);
            _result = new PreferencesEntity(_tmpId,_tmpLastHostelId,_tmpRecentSearchesRaw);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object get(final Continuation<? super PreferencesEntity> $completion) {
    final String _sql = "SELECT * FROM preferences WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PreferencesEntity>() {
      @Override
      @Nullable
      public PreferencesEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLastHostelId = CursorUtil.getColumnIndexOrThrow(_cursor, "last_hostel_id");
          final int _cursorIndexOfRecentSearchesRaw = CursorUtil.getColumnIndexOrThrow(_cursor, "recent_searches");
          final PreferencesEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpLastHostelId;
            if (_cursor.isNull(_cursorIndexOfLastHostelId)) {
              _tmpLastHostelId = null;
            } else {
              _tmpLastHostelId = _cursor.getString(_cursorIndexOfLastHostelId);
            }
            final String _tmpRecentSearchesRaw;
            _tmpRecentSearchesRaw = _cursor.getString(_cursorIndexOfRecentSearchesRaw);
            _result = new PreferencesEntity(_tmpId,_tmpLastHostelId,_tmpRecentSearchesRaw);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
