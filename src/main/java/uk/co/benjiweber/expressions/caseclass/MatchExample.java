package uk.co.benjiweber.expressions.caseclass;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.caseclass.MatchExample.Person;

import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class MatchExample {
    public static void main(String... args) {
        System.out.println(description(Person.person("Bill", "Smith", 18)));
    }

    static String description(Person person) {
        return person.match()
                .when(Person::person, "Bob", _, _).then((lastname, age) -> lastname)
                .when(Person::person, _, _, 18).then((firstname, lastname) -> lastname)
                ._("unknown");
    }

    interface Person extends Case<Person> {
        static Person person(String firstname, String lastname, Integer age) {
            abstract class PersonValue extends Value<Person> implements Person {
            }
            return new PersonValue() {
                @Override
                public String firstname() {
                    return firstname;
                }

                @Override
                public String lastname() {
                    return lastname;
                }

                @Override
                public Integer age() {
                    return age;
                }
            }.using(Person::firstname, Person::lastname, Person::age);
        }

        String firstname();

        String lastname();

        Integer age();
    }
}
