package uk.co.benjiweber.expressions.caseclass;

import uk.co.benjiweber.expressions.Value;

import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class MatchExample {
    interface Person extends Case<Person> {
        String firstname();
        String lastname();
        Integer age();

        static Person person(String firstname, String lastname, Integer age) {
            abstract class PersonValue extends Value<Person> implements Person {}
            return new PersonValue() {
                public String firstname() { return firstname; }
                public String lastname() { return lastname; }
                public Integer age() { return age; }
            }.using(Person::firstname, Person::lastname, Person::age);
        }
    }
    public static void main(String... args) {
        System.out.println(description(Person.person("Bill","Smith",18)));
    }

    static String description(Person person) {
        return person.match()
            .when(Person::person,  "Bob", _, _).then( (lastname, age) -> lastname )
            .when(Person::person,  _,_, 18).then( (firstname, lastname) -> lastname )
            ._("unknown");
    }
}
