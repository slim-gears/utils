package com.slimgears.util.guice;

import javax.inject.Singleton;

@Singleton
@AutoBinding({Dummy.InterfaceA.class, Dummy.InterfaceB.class})
class AutoBindingClass implements Dummy.InterfaceA, Dummy.InterfaceB, Dummy.InterfaceC {
}
