package com.danielgomes.jukebox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jukebox {
    private String id;
    private String model;
    private Set<Component> components;

    public Set<String> componentsAsString(){
        return this.components.stream().map(Component::getName).collect(Collectors.toSet());
    }

}
