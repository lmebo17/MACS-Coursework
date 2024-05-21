SELECT
    j.job_id,
    j.job_title AS position_name,
    CASE WHEN COUNT(CASE WHEN MOD(e.employee_id, 2) != 0 THEN 1 END) = 0 THEN 'N/A' ELSE to_char(COUNT(CASE WHEN MOD(e.employee_id, 2) != 0 THEN 1 END)) END AS emp_cnt,
    NVL(
        to_char(AVG(CASE WHEN mod(e.employee_id, 2) = 0 THEN e.salary END)),
        'N/A'
    ) AS avg_sal,
    CASE WHEN COUNT(CASE WHEN e.salary = (SELECT MAX(salary) FROM employees WHERE job_id = j.job_id) THEN 1 END) = 0 THEN 'N/A' ELSE to_char(COUNT(CASE WHEN e.salary = (SELECT MAX(salary) FROM employees WHERE job_id = j.job_id) THEN 1 END)) END AS mx_cnt
FROM
    jobs j
LEFT JOIN
    employees e ON j.job_id = e.job_id
WHERE LENGTH(j.job_id) >= 4
      AND ((j.max_salary - j.min_salary) <
          (SELECT MAX(max_salary - min_salary) FROM jobs))
GROUP BY
    j.job_id, j.job_title
ORDER BY
    CASE WHEN j.job_id LIKE '%IT%' THEN 0 ELSE 1 END,
    j.job_id;    