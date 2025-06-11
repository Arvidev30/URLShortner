package com.learning.urlshortner.domain.models;

import java.io.Serializable;

public record UserDTO(Long id, String name) implements Serializable {

}
