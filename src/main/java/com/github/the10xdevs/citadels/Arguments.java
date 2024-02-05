package com.github.the10xdevs.citadels;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = "--2thousand")
    public boolean twoThousand;

    @Parameter(names = "--demo")
    public boolean demo;
}
