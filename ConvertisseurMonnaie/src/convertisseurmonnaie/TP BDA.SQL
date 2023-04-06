create or replace function augMILLE(depnum int ) returns void as $$
declare 
nvsal emp.sal %type;
salMAX emp.sal %type;
ligne emp%rowtype;

begin
SELECT max(sal) into salMAX from emp;
for ligne IN SELECT * from emp where deptNO=depnum AND sal>1500 loop 
nvsal :=ligne.sal +1000;
in nvsal <= salMAX then 
update emp set sal= nvsal where empno = linge.empno;
else raise execption "le salaire de % depasse le salaire max %", ligne.ename,salMAX;
end if;
end loop;
end
$$
language plpgsql;
SELECT augMILLE(20);