apply(X,S) :- student(X), scholarship(S).

offer(X,S) :- apply(X,S), not neg_offer(X,S).
neg_offer(X,S) :- apply(X,S), not offer(X,S).

wait_list(X,S) :- apply(X,S), neg_offer(X,S), not out(X,S).
out(X,S) :- apply(X,S), neg_offer(X,S), not wait_list(X,S).

pick(X,S) :- offer(X,S), not student_refuse(X,S).
student_refuse(X,S) :- offer(X,S), not pick(X,S).

refuse(S) :- student_refuse(X,S).

second_offer(X,S) :- wait_list(X,S), refuse(S).

has_wait(S) :- wait_list(X,S).
ok(S) :- scholarship(S), offer(X,S).

:- not has_wait(S), scholarship(S).
:- not ok(S), scholarship(S).
:- offer(X,S), offer(Y,S), different_student(X,Y).
:- wait_list(X,S), wait_list(Y,S), different_student(X,Y).

ok_pick(X) :- pick(X,S).
:- not ok_pick(X), offer(X,S).

:- pick(n2,S1), pick(n2,S2), different_scholarship(S1,S2).
:- pick(n3,S1), pick(n3,S2), different_scholarship(S1,S2).

