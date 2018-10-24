package com.jwcjlu.gateway.core.etcd;

import java.util.List;

public interface ChildListener extends Listener {

    void childChanged(String path, List<String> children);
}
