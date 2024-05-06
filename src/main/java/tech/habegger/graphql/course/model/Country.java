package tech.habegger.graphql.course.model;
public record Country(
    String code,
    String name,
    Integer population,
    Integer area,
    String capital
) {
}
