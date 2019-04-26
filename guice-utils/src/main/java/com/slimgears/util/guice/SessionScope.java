package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.slimgears.util.stream.Lazy;

@SuppressWarnings("WeakerAccess")
public class SessionScope extends AbstractContextScope<SessionScope.Session> {
    private final Lazy<Session> defaultSession = Lazy.of(this::createContext);

    public static class Session extends AbstractContextScope.Context<Session> {
        Session(SessionScope sessionScope) {
            super(sessionScope);
        }
    }

    public SessionScope(Binder binder) {
        super(binder, Session.class);
    }

    @Override
    protected Session createContext() {
        return new Session(this);
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        if (key.getAnnotationType() == DefaultSession.class) {
            return () -> defaultSession.get().getObject(Key.get(key.getTypeLiteral()), unscoped);
        } else {
            return super.scope(key, unscoped);
        }
    }

    public static SessionScope install(Binder binder) {
        SessionScope scope = new SessionScope(binder);
        binder.bindScope(SessionScoped.class, scope);
        binder.bind(SessionScope.class).toInstance(scope);
        binder.bind(Key.get(Session.class, DefaultSession.class)).toProvider(scope.defaultSession::get);
        return scope;
    }
}
