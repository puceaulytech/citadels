package com.github.the10xdevs.citadels;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = {"--2thousand", "--2thousands"})
    public boolean twoThousand;

    @Parameter(names = "--demo")
    public boolean demo;

    @Parameter(names = "--csv")
    public boolean csv;
}
